import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { AuthenticationResponseDto } from '../../models/authentication-response-dto';
import { SignInRequestDto } from '../../models/sign-in-request-dto';

export interface SignIn$Params {
      body: SignInRequestDto
}

export function signIn(http: HttpClient, rootUrl: string, params: SignIn$Params, context?: HttpContext): Observable<StrictHttpResponse<AuthenticationResponseDto>> {
  const rb = new RequestBuilder(rootUrl, signIn.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<AuthenticationResponseDto>;
    })
  );
}

signIn.PATH = '/api/v1/auth/sign-in';
