import {HttpClient, HttpContext} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {BaseService} from '../base-service';
import {ApiConfiguration} from '../api-configuration';
import {StrictHttpResponse} from '../strict-http-response';
import {refreshToken} from '../fn/authentication-controller/refresh-token';
import {signIn} from '../fn/authentication-controller/sign-in';
import {SignIn$Params} from '../fn/authentication-controller/sign-in';
import {signOut} from '../fn/authentication-controller/sign-out';
import {SignOut$Params} from '../fn/authentication-controller/sign-out';
import {StorageService} from '../storage/storage.service';
import {ErrorHandlingService} from '../error-handling/error-handling.service';
import {TokenService} from '../token/token.service';
import {SignInRequestDto} from '../models/sign-in-request-dto';

/**
 * API for authentication, communicating with the Authentication microservice.
 */
@Injectable({providedIn: 'root'})
export class AuthenticationControllerService extends BaseService {
  constructor(
    config: ApiConfiguration,
    http: HttpClient,
    private tokenService: TokenService,
    private errorHandlingService: ErrorHandlingService,
    private storageService: StorageService
  ) {
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
  signOut$Response(params: SignOut$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return signOut(this.http, this.rootUrl, params, context, this.tokenService, this.storageService);
  }

  /**
   * Sign out and invalidate the user.
   *
   * Invalidates the current user by communicating with the Authentication microservice.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), use `signOut$Response()` instead.
   */
  signOut(params: SignOut$Params, context?: HttpContext): Observable<void> {
    return this.signOut$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

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
  signIn$Response(params: SignIn$Params, context?: HttpContext): Observable<void> {
    return signIn( this.tokenService, this.storageService, this.errorHandlingService, this.http, this.rootUrl, params, context);
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
  signIn(signInRequest: SignInRequestDto, context?: HttpContext): Observable<void> {
    const params = { body: signInRequest };
    return this.signIn$Response(params, context).pipe(
      map((response: any) => {return response;}));
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
  refreshToken$Response(params?: any, context?: HttpContext): Observable<any> {
    return refreshToken(this.tokenService, this.storageService, this.http, this.rootUrl, params, context);
  }

  /**
   * Refresh JWT token with custom headers.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), use `refreshToken$Response()` instead.
   */
  refreshToken(params?: any, context?: HttpContext): Observable<any> {
    return this.refreshToken$Response(params, context).pipe(
      map((response: any) => {return response;}));
  }
}
