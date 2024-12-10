import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface AttachBase64FileToInvoice$Params {
  invoiceId: string;
      body?: {
'file': Blob;
}
}

export function attachBase64FileToInvoice(http: HttpClient, rootUrl: string, params: AttachBase64FileToInvoice$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
  const rb = new RequestBuilder(rootUrl, attachBase64FileToInvoice.PATH, 'post');
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

attachBase64FileToInvoice.PATH = '/api/v1/invoice/attach-base64-file/{invoiceId}';
