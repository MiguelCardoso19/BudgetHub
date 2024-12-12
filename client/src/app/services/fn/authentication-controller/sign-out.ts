import {HttpClient, HttpContext, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {filter, map} from 'rxjs/operators';
import {StrictHttpResponse} from '../../strict-http-response';
import {RequestBuilder} from '../../request-builder';
import {AuthenticationControllerService} from '../../services';

export interface SignOut$Params {
}

export function signOut(http: HttpClient, rootUrl: string, params?: SignOut$Params, context?: HttpContext, headers?: HttpHeaders): Observable<StrictHttpResponse<void>> {
  const rb = new RequestBuilder(rootUrl, AuthenticationControllerService.SignOutPath, 'post');

  if (headers) {
    rb.header('Authorization', headers.get('Authorization') || '');
    rb.header('Nif', headers.get('Nif') || '');
  }

  return http.request(
    rb.build({responseType: 'text', accept: '*/*', context})
  ).pipe(
    filter((event: any): event is HttpResponse<any> => event instanceof HttpResponse),
    map((r: HttpResponse<any>) => r.clone({body: undefined}) as StrictHttpResponse<void>)
  );
}

signOut.PATH = '/api/v1/auth/sign-out';
