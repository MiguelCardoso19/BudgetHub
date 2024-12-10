import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { SessionRequestDto } from '../../models/session-request-dto';
import { SessionResponseDto } from '../../models/session-response-dto';

export interface CreatePaymentSession$Params {
      body: SessionRequestDto
}

export function createPaymentSession(http: HttpClient, rootUrl: string, params: CreatePaymentSession$Params, context?: HttpContext): Observable<StrictHttpResponse<SessionResponseDto>> {
  const rb = new RequestBuilder(rootUrl, createPaymentSession.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<SessionResponseDto>;
    })
  );
}

createPaymentSession.PATH = '/api/v1/payment/create-payment-session';
