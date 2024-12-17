import { HttpClient, HttpContext, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { filter, map, catchError } from 'rxjs/operators';
import { RequestBuilder } from '../../request-builder';
import { AuthenticationControllerService } from '../../services';
import { TokenService } from '../../token/token.service';
import { StorageService } from '../../storage/storage.service';

export function refreshToken(
  tokenService: TokenService,
  storageService: StorageService,
  http: HttpClient,
  rootUrl: string,
  params?: any,
  context?: HttpContext,
): Observable<{ token: string; refreshToken: string }> {

  const headers = new HttpHeaders({
    'Authorization': `Bearer ${tokenService.refreshToken}`,
    'Nif': storageService.nif || ''
  });

  const rb = new RequestBuilder(rootUrl, AuthenticationControllerService.RefreshTokenPath, 'post');

  rb.header('Authorization', headers.get('Authorization') || '');
  rb.header('Nif', headers.get('Nif') || '');

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((event: any): event is HttpResponse<any> => event instanceof HttpResponse),
    map((response: HttpResponse<any>) => {
      return {
        token: response.body?.token,
        refreshToken: response.body?.refreshToken
      };
    }),
    catchError(err => {
      tokenService.removeToken();
      tokenService.removeRefreshToken();
      storageService.removeId();
      storageService.removeName();
      storageService.removeNif();
      return throwError(() => err);
    })
  );
}
