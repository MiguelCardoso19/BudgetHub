import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { CustomPageDtoObject } from '../../models/custom-page-dto-object';
import { Pageable } from '../../models/pageable';

export interface FindAllBudgetTypes$Params {
  pageable: Pageable;
}

export function findAllBudgetTypes(http: HttpClient, rootUrl: string, params: FindAllBudgetTypes$Params, context?: HttpContext): Observable<StrictHttpResponse<CustomPageDtoObject>> {
  const rb = new RequestBuilder(rootUrl, findAllBudgetTypes.PATH, 'get');
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

findAllBudgetTypes.PATH = '/api/v1/budget/type/all';
