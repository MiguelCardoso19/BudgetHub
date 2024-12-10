import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { MovementDto } from '../../models/movement-dto';

export interface Update2$Params {
      body: MovementDto
}

export function update2(http: HttpClient, rootUrl: string, params: Update2$Params, context?: HttpContext): Observable<StrictHttpResponse<MovementDto>> {
  const rb = new RequestBuilder(rootUrl, update2.PATH, 'put');
  if (params) {
    rb.body(params.body, 'application/json');
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

update2.PATH = '/api/v1/movement/update';
