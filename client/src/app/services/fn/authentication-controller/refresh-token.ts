import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { AuthenticationResponseDto } from '../../models/authentication-response-dto';

export interface RefreshToken$Params {
}

export function refreshToken(http: HttpClient, rootUrl: string, params?: RefreshToken$Params, context?: HttpContext): Observable<StrictHttpResponse<AuthenticationResponseDto>> {
  const rb = new RequestBuilder(rootUrl, refreshToken.PATH, 'post');
  if (params) {
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

refreshToken.PATH = '/api/v1/auth/refresh-token';
