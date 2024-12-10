import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { BudgetSubtypeDto } from '../../models/budget-subtype-dto';

export interface UpdateSubtype$Params {
      body: BudgetSubtypeDto | null
}

export function updateSubtype(http: HttpClient, rootUrl: string, params: UpdateSubtype$Params, context?: HttpContext): Observable<StrictHttpResponse<BudgetSubtypeDto | null>> {
  const rb = new RequestBuilder(rootUrl, updateSubtype.PATH, 'put');
  if (params) {
    rb.body(params.body, 'application/json');
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

updateSubtype.PATH = '/api/v1/budget/subtype/update';
