import {HttpClient, HttpContext, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {config, Observable, of} from 'rxjs';
import {catchError, map, switchMap} from 'rxjs/operators';
import {BaseService} from '../base-service';
import {ApiConfiguration} from '../api-configuration';
import {StrictHttpResponse} from '../strict-http-response';
import {AuthenticationResponseDto} from '../models/authentication-response-dto';
import {refreshToken} from '../fn/authentication-controller/refresh-token';
import {signIn} from '../fn/authentication-controller/sign-in';
import {SignIn$Params} from '../fn/authentication-controller/sign-in';
import {signOut} from '../fn/authentication-controller/sign-out';
import {SignOut$Params} from '../fn/authentication-controller/sign-out';
import {SignInRequestDto} from '../models/sign-in-request-dto';
import {RequestBuilder} from '../request-builder';
import {Router} from '@angular/router';
import {TokenService} from '../token/token.service';
import {ErrorHandlingService} from '../error-handling/error-handling.service';
import {StorageService} from '../storage/storage.service';

/**
 * API for authentication, communicating with the Authentication microservice.
 */
@Injectable({providedIn: 'root'})
export class AuthenticationService {

  constructor(private http: HttpClient,
              private router: Router,
              private tokenService: TokenService,
              private errorHandlingService: ErrorHandlingService,
              private storageService: StorageService) {
  }

  /** Path part for operation `signOut()` */
  static readonly SignOutPath = '/api/v1/auth/sign-out';

  /**
   * Sign out and invalidate the user.
   *
   * Invalidates the current user by communicating with the Authentication microservice.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `signOut()` instead.
   *
   * This method doesn't expect any request body.
   */
 /* signOut$Response(params: SignOut$Params, context?: HttpContext, headers?: HttpHeaders): Observable<StrictHttpResponse<void>> {
    return signOut(this.http, this.rootUrl, params, context, headers);
  }

  signOut(params: SignOut$Params, context?: HttpContext, headers?: HttpHeaders): Observable<void> {
    return this.signOut$Response(params, context, headers).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  } */

  /** Path part for operation `signIn()` */
  private readonly signInPath = 'http://localhost:8080/api/v1/auth/sign-in';

  /**
   * Sign in using user credentials.
   *
   * Authenticate a user through the proxy and return a JWT token if successful.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `signIn()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */

  login(signInRequest: SignInRequestDto): Observable<void> {
    const rb = new RequestBuilder('', this.signInPath, 'post');
    rb.body(signInRequest, 'application/json');

    return this.http.request(rb.build({ responseType: 'blob', accept: '*/*' })).pipe(
      switchMap((response: any) => {
        if (response instanceof Blob) {
          return response.text().then((text: string) => {
            const parsedResponse = JSON.parse(text);

            // Update tokens and user details
            this.tokenService.token = parsedResponse.token as string;
            this.tokenService.refreshToken = parsedResponse.refreshToken as string;
            this.storageService.id = parsedResponse.id as string;
            this.storageService.nif = parsedResponse.nif as string;
            this.storageService.name = parsedResponse.firstName as string;

            // Navigate to the dashboard
            return this.router.navigate(['dashboard']);
          });
        }
        return Promise.resolve(); // Ensure consistent return type
      }),
      map(() => undefined), // Ensure the observable emits `void`
      catchError((err) => {
        throw this.errorHandlingService.handleError(err);
      })
    );
  }


  /** Path part for operation `refreshToken()` */
  static readonly RefreshTokenPath = '/api/v1/auth/refresh-token';

  /**
   * Refresh JWT token.
   *
   * Refresh the JWT token using the Authorization header through the proxy.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `refreshToken()` instead.
   *
   * This method doesn't expect any request body.
   */
  refreshToken$Response(params?: any, context?: HttpContext, headers?: HttpHeaders): Observable<any> {
    return refreshToken(this.http, 'this.rootUrl', params, context, headers);
  }


  refreshToken(params?: any, context?: HttpContext, headers?: HttpHeaders): Observable<any> {
    return this.refreshToken$Response(params, context, headers).pipe(
      map((response: any) => response)
    );
  }
}
