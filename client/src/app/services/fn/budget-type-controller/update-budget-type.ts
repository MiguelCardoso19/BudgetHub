import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { BudgetTypeDto } from '../../models/budget-type-dto';

export interface UpdateBudgetType$Params {
      body: BudgetTypeDto | null
}

export function updateBudgetType(http: HttpClient, rootUrl: string, params: UpdateBudgetType$Params, context?: HttpContext): Observable<StrictHttpResponse<BudgetTypeDto | null>> {
  const rb = new RequestBuilder(rootUrl, updateBudgetType.PATH, 'put');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<BudgetTypeDto | null>;
    })
  );
}

updateBudgetType.PATH = '/api/v1/budget/type/update';
