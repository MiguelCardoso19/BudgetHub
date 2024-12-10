import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { StripeCardTokenDto } from '../../models/stripe-card-token-dto';

export interface CreateCardToken$Params {
      body: StripeCardTokenDto
}

export function createCardToken(http: HttpClient, rootUrl: string, params: CreateCardToken$Params, context?: HttpContext): Observable<StrictHttpResponse<StripeCardTokenDto>> {
  const rb = new RequestBuilder(rootUrl, createCardToken.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<StripeCardTokenDto>;
    })
  );
}

createCardToken.PATH = '/api/v1/payment/create-card-token';
