import { HttpClient, HttpContext, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { filter, map, catchError } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';
import { BudgetTypeDto } from '../../models/budget-type-dto';
import { TokenService } from '../../token/token.service';
import { StorageService } from '../../storage/storage.service';

export interface UpdateBudgetType$Params {
  body: BudgetTypeDto | null;
}

export function updateBudgetType(
  tokenService: TokenService,
  storageService: StorageService,
  http: HttpClient,
  rootUrl: string,
  params: UpdateBudgetType$Params,
  context?: HttpContext
): Observable<StrictHttpResponse<BudgetTypeDto | null>> {
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${tokenService.token || ''}`,
    'Nif': storageService.nif || ''
  });

  const rb = new RequestBuilder(rootUrl, updateBudgetType.PATH, 'put');

  rb.header('Authorization', headers.get('Authorization') || '');
  rb.header('Nif', headers.get('Nif') || '');

  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((event: any): event is HttpResponse<any> => event instanceof HttpResponse),
    map((response: HttpResponse<any>) => {
      return response as StrictHttpResponse<BudgetTypeDto | null>;
    }),
    catchError(err => {
      return throwError(() => err);
    })
  );
}

updateBudgetType.PATH = '/api/v1/budget/type/update';
