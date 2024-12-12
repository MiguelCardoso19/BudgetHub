import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { UserCredentialsDto } from '../../models/user-credentials-dto';

export interface FindById$Params {
  id: string;
}

export function findById(http: HttpClient, rootUrl: string, params: FindById$Params, context?: HttpContext): Observable<StrictHttpResponse<UserCredentialsDto>> {
  const rb = new RequestBuilder(rootUrl, findById.PATH, 'get');
  if (params) {
    rb.query('id', params.id);
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return (r as HttpResponse<any>).clone({ body: r.body }) as StrictHttpResponse<UserCredentialsDto>;
    })
  );
}

findById.PATH = '/api/v1/user-credentials/get-user-by-id';
