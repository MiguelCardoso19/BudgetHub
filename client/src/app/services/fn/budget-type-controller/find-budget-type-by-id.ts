import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { BudgetTypeDto } from '../../models/budget-type-dto';

export interface FindBudgetTypeById$Params {
  id: string;
}

export function findBudgetTypeById(http: HttpClient, rootUrl: string, params: FindBudgetTypeById$Params, context?: HttpContext): Observable<StrictHttpResponse<BudgetTypeDto | null>> {
  const rb = new RequestBuilder(rootUrl, findBudgetTypeById.PATH, 'get');
  if (params) {
    rb.path('id', params.id, {});
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

findBudgetTypeById.PATH = '/api/v1/budget/type/{id}';
