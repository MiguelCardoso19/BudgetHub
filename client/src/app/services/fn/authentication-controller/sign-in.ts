import {HttpClient, HttpContext, HttpResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {filter, map, catchError, switchMap} from 'rxjs/operators';
import {RequestBuilder} from '../../request-builder';
import {SignInRequestDto} from '../../models/sign-in-request-dto';
import {TokenService} from '../../token/token.service';
import {StorageService} from '../../storage/storage.service';
import {ErrorHandlingService} from '../../error-handling/error-handling.service';

export interface SignIn$Params {
  body: SignInRequestDto;
}

export function signIn(
  tokenService: TokenService,
  storageService: StorageService,
  errorHandlingService: ErrorHandlingService,
  http: HttpClient,
  rootUrl: string,
  params: SignIn$Params,
  context?: HttpContext,
): Observable<void> {
  const rb = new RequestBuilder(rootUrl, signIn.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  // @ts-ignore
  return http.request(rb.build({responseType: 'blob', accept: '*/*', context})).pipe(filter((r: any): r is HttpResponse<Blob> => r instanceof HttpResponse), map((response: HttpResponse<Blob>) => response.body), map((blob: Blob) => {
      return new Promise<void>((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => {
          try {
            const parsedResponse = JSON.parse(reader.result as string);
            tokenService.token = parsedResponse.token as string;
            tokenService.refreshToken = parsedResponse.refreshToken as string;
            storageService.id = parsedResponse.id as string;
            storageService.nif = parsedResponse.nif as string;
            storageService.name = parsedResponse.firstName as string;
            resolve();
          } catch (err) {
            reject(['Error parsing response: ' + err]);
          }
        };
        reader.onerror = () => {
          reject(['Error reading the Blob response: ' + reader.error?.message]);
        };
        reader.readAsText(blob);
      });
    }),
    switchMap((promise: Promise<void>) => promise),
    catchError((err) => {
      const errorMessages = errorHandlingService.handleError(err);
      return throwError(() => errorMessages);
    })
  );
}

signIn.PATH = '/api/v1/auth/sign-in';
