import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface AttachMultipartFileToInvoice$Params {
  invoiceId: string;
      body?: {
'file': Blob;
}
}

export function attachMultipartFileToInvoice(http: HttpClient, rootUrl: string, params: AttachMultipartFileToInvoice$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
  const rb = new RequestBuilder(rootUrl, attachMultipartFileToInvoice.PATH, 'post');
  if (params) {
    rb.path('invoiceId', params.invoiceId, {});
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'text', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return (r as HttpResponse<any>).clone({ body: undefined }) as StrictHttpResponse<void>;
    })
  );
}

attachMultipartFileToInvoice.PATH = '/api/v1/invoice/attach-multipart-file/{invoiceId}';
