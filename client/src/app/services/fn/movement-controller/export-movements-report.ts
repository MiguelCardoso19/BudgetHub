import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface ExportMovementsReport$Params {
  startDate?: string;
  endDate?: string;
  status?: 'ACCEPTED' | 'PROCESSING' | 'REFUSED' | 'CANCELED' | 'FAILED' | 'REFUNDED' | 'SUCCEEDED';
}

export function exportMovementsReport(http: HttpClient, rootUrl: string, params?: ExportMovementsReport$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
  const rb = new RequestBuilder(rootUrl, exportMovementsReport.PATH, 'post');
  if (params) {
    rb.query('startDate', params.startDate, {});
    rb.query('endDate', params.endDate, {});
    rb.query('status', params.status, {});
  }

  return http.request(
    rb.build({ responseType: 'text', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return (r as HttpResponse<any>).clone({ body: undefined }) as StrictHttpResponse<void>;
    })
  );
}

exportMovementsReport.PATH = '/api/v1/movement/export-movements-report';
