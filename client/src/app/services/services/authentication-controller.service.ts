import {HttpClient, HttpContext, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {BaseService} from '../base-service';
import {ApiConfiguration} from '../api-configuration';
import {StrictHttpResponse} from '../strict-http-response';
import {AuthenticationResponseDto} from '../models/authentication-response-dto';
import {refreshToken} from '../fn/authentication-controller/refresh-token';
import {signIn} from '../fn/authentication-controller/sign-in';
import {SignIn$Params} from '../fn/authentication-controller/sign-in';
import {signOut} from '../fn/authentication-controller/sign-out';
import {SignOut$Params} from '../fn/authentication-controller/sign-out';

/**
 * API for authentication, communicating with the Authentication microservice.
 */
@Injectable({providedIn: 'root'})
export class AuthenticationControllerService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
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
  signOut$Response(params: SignOut$Params, context?: HttpContext, headers?: HttpHeaders): Observable<StrictHttpResponse<void>> {
    return signOut(this.http, this.rootUrl, params, context, headers);
  }

  /**
   * Sign out and invalidate the user.
   *
   * Invalidates the current user by communicating with the Authentication microservice.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), use `signOut$Response()` instead.
   */
  signOut(params: SignOut$Params, context?: HttpContext, headers?: HttpHeaders): Observable<void> {
    return this.signOut$Response(params, context, headers).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `signIn()` */
  static readonly SignInPath = '/api/v1/auth/sign-in';

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
  signIn$Response(params: SignIn$Params, context?: HttpContext): Observable<StrictHttpResponse<AuthenticationResponseDto>> {
    return signIn(this.http, this.rootUrl, params, context);
  }

  /**
   * Sign in using user credentials.
   *
   * Authenticate a user through the proxy and return a JWT token if successful.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `signIn$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  signIn(params: SignIn$Params, context?: HttpContext): Observable<AuthenticationResponseDto> {
    return this.signIn$Response(params, context).pipe(
      map((r: StrictHttpResponse<AuthenticationResponseDto>): AuthenticationResponseDto => r.body)
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
    return refreshToken(this.http, this.rootUrl, params, context, headers);
  }

  /**
   * Refresh JWT token with custom headers.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), use `refreshToken$Response()` instead.
   */
  refreshToken(params?: any, context?: HttpContext, headers?: HttpHeaders): Observable<any> {
    return this.refreshToken$Response(params, context, headers).pipe(
      map((response: any) => response)
    );
  }
}
