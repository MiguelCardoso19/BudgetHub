import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { CreatePaymentDto } from '../../models/create-payment-dto';
import { CreatePaymentResponseDto } from '../../models/create-payment-response-dto';

export interface CreatePaymentIntent$Params {
      body: CreatePaymentDto
}

export function createPaymentIntent(http: HttpClient, rootUrl: string, params: CreatePaymentIntent$Params, context?: HttpContext): Observable<StrictHttpResponse<CreatePaymentResponseDto>> {
  const rb = new RequestBuilder(rootUrl, createPaymentIntent.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<CreatePaymentResponseDto>;
    })
  );
}

createPaymentIntent.PATH = '/api/v1/payment/create-payment-intent';
