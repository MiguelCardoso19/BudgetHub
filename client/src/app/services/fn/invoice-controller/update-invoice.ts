import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { InvoiceDto } from '../../models/invoice-dto';

export interface UpdateInvoice$Params {
      body: InvoiceDto | null
}

export function updateInvoice(http: HttpClient, rootUrl: string, params: UpdateInvoice$Params, context?: HttpContext): Observable<StrictHttpResponse<InvoiceDto | null>> {
  const rb = new RequestBuilder(rootUrl, updateInvoice.PATH, 'put');
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

updateInvoice.PATH = '/api/v1/invoice/update';
