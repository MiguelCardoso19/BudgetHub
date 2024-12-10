import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { BudgetSubtypeDto } from '../../models/budget-subtype-dto';

export interface FindSubtypeById$Params {
  id: string;
}

export function findSubtypeById(http: HttpClient, rootUrl: string, params: FindSubtypeById$Params, context?: HttpContext): Observable<StrictHttpResponse<BudgetSubtypeDto | null>> {
  const rb = new RequestBuilder(rootUrl, findSubtypeById.PATH, 'get');
  if (params) {
    rb.path('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<BudgetSubtypeDto | null>;
    })
  );
}

findSubtypeById.PATH = '/api/v1/budget/subtype/{id}';
