import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { SupplierDto } from '../../models/supplier-dto';

export interface Create$Params {
      body: SupplierDto
}

export function create(http: HttpClient, rootUrl: string, params: Create$Params, context?: HttpContext): Observable<StrictHttpResponse<SupplierDto>> {
  const rb = new RequestBuilder(rootUrl, create.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<SupplierDto>;
    })
  );
}

create.PATH = '/api/v1/supplier/create';
