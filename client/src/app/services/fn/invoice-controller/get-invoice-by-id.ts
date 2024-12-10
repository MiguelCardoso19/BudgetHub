import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { InvoiceDto } from '../../models/invoice-dto';

export interface GetInvoiceById$Params {
  id: string;
}

export function getInvoiceById(http: HttpClient, rootUrl: string, params: GetInvoiceById$Params, context?: HttpContext): Observable<StrictHttpResponse<InvoiceDto | null>> {
  const rb = new RequestBuilder(rootUrl, getInvoiceById.PATH, 'get');
  if (params) {
    rb.path('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<InvoiceDto | null>;
    })
  );
}

getInvoiceById.PATH = '/api/v1/invoice/{id}';
