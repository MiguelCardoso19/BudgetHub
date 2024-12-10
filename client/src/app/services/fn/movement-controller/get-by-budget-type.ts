import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { CustomPageDtoObject } from '../../models/custom-page-dto-object';
import { Pageable } from '../../models/pageable';

export interface GetByBudgetType$Params {
  budgetTypeId: string;
  pageable: Pageable;
}

export function getByBudgetType(http: HttpClient, rootUrl: string, params: GetByBudgetType$Params, context?: HttpContext): Observable<StrictHttpResponse<CustomPageDtoObject>> {
  const rb = new RequestBuilder(rootUrl, getByBudgetType.PATH, 'get');
  if (params) {
    rb.path('budgetTypeId', params.budgetTypeId, {});
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

getByBudgetType.PATH = '/api/v1/movement/budget-type/{budgetTypeId}';
