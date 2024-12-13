import { HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenService } from '../services/token/token.service';
import { catchError, switchMap } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { Router } from '@angular/router';
import {StorageService} from '../services/id/storage.service';

export function authInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const authService = inject(TokenService);
  const storageService = inject(StorageService);

  if (req.url.includes('/sign-in') || req.url.includes('/register') || req.url.includes('/refresh-token')) {
    return next(req);
  }

  if (authService.isAuthenticatedAndNotExpired()) {
    const token = authService.token;
    req = addHeaders(req, token, storageService.nif);
    return next(req);
  } else {
    return handleExpiredToken(req, next);
  }
}

function addHeaders(req: HttpRequest<unknown>, token: string, nif: string) {
  return req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
      Nif: nif,
    }
  });
}

function handleExpiredToken(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const tokenService = inject(TokenService);
  const router = inject(Router);
  const storageService = inject(StorageService);

  return tokenService.refreshTokenRequest(storageService.nif).pipe(
    switchMap((res: any) => {
      const { token, refreshToken } = res;
      tokenService.token = token;
      tokenService.refreshToken = refreshToken;
      req = addHeaders(req, token, storageService.nif);
      return next(req);
    }),
    catchError(err => {
      tokenService.removeToken();
      tokenService.removeRefreshToken();
      storageService.removeId();
      storageService.removeName();
      storageService.removeNif();
      router.navigate(['login']);
      return throwError(() => err);
    })
  );
}
