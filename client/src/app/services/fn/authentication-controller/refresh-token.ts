import { HttpClient, HttpContext, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { filter, map, catchError } from 'rxjs/operators';
import { RequestBuilder } from '../../request-builder';
import { AuthenticationControllerService } from '../../services';

export function refreshToken(
  http: HttpClient,
  rootUrl: string,
  params?: any,
  context?: HttpContext,
  headers?: HttpHeaders
): Observable<{ token: string; refreshToken: string }> {
  const rb = new RequestBuilder(rootUrl, AuthenticationControllerService.RefreshTokenPath, 'post');

  if (headers) {
    rb.header('Authorization', headers.get('Authorization') || '');
    rb.header('Nif', headers.get('Nif') || '');
  }

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
      console.error('Error during refreshToken:', err);
      return throwError(() => err);
    })
  );
}
