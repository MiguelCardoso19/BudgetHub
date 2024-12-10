import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { StripeSepaTokenDto } from '../../models/stripe-sepa-token-dto';

export interface CreateSepaToken$Params {
      body: StripeSepaTokenDto
}

export function createSepaToken(http: HttpClient, rootUrl: string, params: CreateSepaToken$Params, context?: HttpContext): Observable<StrictHttpResponse<StripeSepaTokenDto>> {
  const rb = new RequestBuilder(rootUrl, createSepaToken.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<StripeSepaTokenDto>;
    })
  );
}

createSepaToken.PATH = '/api/v1/payment/create-sepa-token';
