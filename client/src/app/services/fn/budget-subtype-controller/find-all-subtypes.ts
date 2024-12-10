import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { CustomPageDtoObject } from '../../models/custom-page-dto-object';
import { Pageable } from '../../models/pageable';

export interface FindAllSubtypes$Params {
  pageable: Pageable;
}

export function findAllSubtypes(http: HttpClient, rootUrl: string, params: FindAllSubtypes$Params, context?: HttpContext): Observable<StrictHttpResponse<CustomPageDtoObject>> {
  const rb = new RequestBuilder(rootUrl, findAllSubtypes.PATH, 'get');
  if (params) {
    rb.query('pageable', params.pageable, {});
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<CustomPageDtoObject>;
    })
  );
}

findAllSubtypes.PATH = '/api/v1/budget/subtype/all';
