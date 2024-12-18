import { HttpClient, HttpContext, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map, catchError } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

export interface RecoverPassword$Params {
  email: string;
}

export function recoverPassword(
  http: HttpClient,
  rootUrl: string,
  params: RecoverPassword$Params,
  context?: HttpContext
): Observable<StrictHttpResponse<void>> {
  const httpParams = new HttpParams().set('email', params.email);
  const rb = new RequestBuilder(rootUrl, `${recoverPassword.PATH}?${httpParams.toString()}`, 'POST');

  return http.request(rb.build({ responseType: 'text', accept: '*/*', context })).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return (r as HttpResponse<any>).clone({ body: undefined }) as StrictHttpResponse<void>;
    }),
    catchError((error) => {
      throw error;
    })
  );
}

recoverPassword.PATH = '/api/v1/user-credentials/recover-password';
