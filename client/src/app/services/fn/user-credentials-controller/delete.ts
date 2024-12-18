import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map, tap } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { DeleteRequestDto } from '../../models/delete-request-dto';
import {TokenService} from '../../token/token.service';

export interface Delete$Params {
  body: DeleteRequestDto;
}

export function delete$(
  http: HttpClient,
  rootUrl: string,
  params: Delete$Params,
  context?: HttpContext,
  tokenService?: TokenService
): Observable<StrictHttpResponse<void>> {
  const rb = new RequestBuilder(rootUrl, delete$.PATH, 'delete');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'text', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return (r as HttpResponse<any>).clone({ body: undefined }) as StrictHttpResponse<void>;
    }),
    tap(() => {
      if (tokenService) {
        tokenService.removeToken();
        tokenService.removeRefreshToken();
      }
    })
  );
}

delete$.PATH = '/api/v1/user-credentials/delete';
