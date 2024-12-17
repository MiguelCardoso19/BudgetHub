import {HttpHandlerFn, HttpRequest} from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenService } from '../services/token/token.service';
import { catchError, switchMap } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { Router } from '@angular/router';
import {StorageService} from '../services/storage/storage.service';
import {AuthenticationControllerService} from '../services/services';

export function authInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const tokenService: TokenService = inject(TokenService);
  const storageService: StorageService = inject(StorageService);

  if (req.url.includes('/sign-in') || req.url.includes('/register') || req.url.includes('/refresh-token')) {
    return next(req);
  }

  if (tokenService.isAuthenticatedAndNotExpired()) {
    req = addHeaders(req, tokenService.token, storageService.nif);
    return next(req);
  } else {
    return handleExpiredToken(req, next, tokenService, storageService);
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

function handleExpiredToken(req: HttpRequest<unknown>, next: HttpHandlerFn, tokenService: TokenService, storageService: StorageService) {
  const router: Router = inject(Router);
  const authService: AuthenticationControllerService = inject(AuthenticationControllerService);

  return authService.refreshToken({}, undefined).pipe(
    switchMap((res: any) => {
      const { token, refreshToken } = res;
      tokenService.token = token;
      tokenService.refreshToken = refreshToken;
      req = addHeaders(req, token, storageService.nif);
      return next(req);
    }),
     catchError(err => {
       if (err.status === 403) {
         tokenService.removeToken();
         tokenService.removeRefreshToken();
         storageService.removeId();
         storageService.removeName();
         storageService.removeNif();
         router.navigate(['login']);
       }
       return throwError(() => err);
   })
  );
}
