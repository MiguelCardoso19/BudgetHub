import { HttpClient, HttpContext, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map, catchError } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';
import { AuthenticationControllerService } from '../../services';
import { TokenService } from '../../token/token.service';
import { StorageService } from '../../storage/storage.service';

export interface SignOut$Params {}

export function signOut(
  http: HttpClient,
  rootUrl: string,
  params?: SignOut$Params,
  context?: HttpContext,
  tokenService?: TokenService,
  storageService?: StorageService
): Observable<StrictHttpResponse<void>> {
  const rb = new RequestBuilder(rootUrl, AuthenticationControllerService.SignOutPath, 'post');

  const headers = new HttpHeaders({
    'Authorization': 'Bearer ' + (tokenService?.token || ''),
    'Nif': storageService?.nif || ''
  });

  rb.header('Authorization', headers.get('Authorization') || '');
  rb.header('Nif', headers.get('Nif') || '');

  // @ts-ignore
  return http.request(rb.build({ responseType: 'text', accept: '*/*', context })).pipe(
    filter((event: any): event is HttpResponse<any> => event instanceof HttpResponse),
    map((r: HttpResponse<any>) => r.clone({ body: undefined }) as StrictHttpResponse<void>),
    catchError((error) => {
      console.error('Sign-out error:', error);
      throw error;
    }),
    map(() => {
      if (tokenService && storageService) {
        tokenService.removeToken();
        tokenService.removeRefreshToken();
        storageService.removeId();
        storageService.removeName();
        storageService.removeNif();
      }
      return;
    })
  );
}

signOut.PATH = '/api/v1/auth/sign-out';
