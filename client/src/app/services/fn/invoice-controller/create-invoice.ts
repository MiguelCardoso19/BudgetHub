import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { InvoiceDto } from '../../models/invoice-dto';

export interface CreateInvoice$Params {
      body: InvoiceDto | null
}

export function createInvoice(http: HttpClient, rootUrl: string, params: CreateInvoice$Params, context?: HttpContext): Observable<StrictHttpResponse<InvoiceDto | null>> {
  const rb = new RequestBuilder(rootUrl, createInvoice.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
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

createInvoice.PATH = '/api/v1/invoice/create';
