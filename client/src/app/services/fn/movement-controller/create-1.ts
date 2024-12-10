import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { MovementDto } from '../../models/movement-dto';

export interface Create1$Params {
      body: MovementDto
}

export function create1(http: HttpClient, rootUrl: string, params: Create1$Params, context?: HttpContext): Observable<StrictHttpResponse<MovementDto>> {
  const rb = new RequestBuilder(rootUrl, create1.PATH, 'post');
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

create1.PATH = '/api/v1/movement/create';
