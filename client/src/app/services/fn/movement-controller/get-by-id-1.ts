import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { MovementDto } from '../../models/movement-dto';

export interface GetById1$Params {
  id: string;
}

export function getById1(http: HttpClient, rootUrl: string, params: GetById1$Params, context?: HttpContext): Observable<StrictHttpResponse<MovementDto>> {
  const rb = new RequestBuilder(rootUrl, getById1.PATH, 'get');
  if (params) {
    rb.path('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<MovementDto>;
    })
  );
}

getById1.PATH = '/api/v1/movement/{id}';
