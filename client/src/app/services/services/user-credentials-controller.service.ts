import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { AuthenticationResponseDto } from '../models/authentication-response-dto';
import { delete$ } from '../fn/user-credentials-controller/delete';
import { Delete$Params } from '../fn/user-credentials-controller/delete';
import { recoverPassword } from '../fn/user-credentials-controller/recover-password';
import { RecoverPassword$Params } from '../fn/user-credentials-controller/recover-password';
import { register } from '../fn/user-credentials-controller/register';
import { Register$Params } from '../fn/user-credentials-controller/register';
import { resetPassword } from '../fn/user-credentials-controller/reset-password';
import { ResetPassword$Params } from '../fn/user-credentials-controller/reset-password';
import { update } from '../fn/user-credentials-controller/update';
import { Update$Params } from '../fn/user-credentials-controller/update';
import { UserCredentialsDto } from '../models/user-credentials-dto';


/**
 * API for managing user credentials including registration, update, and deletion.
 */
@Injectable({ providedIn: 'root' })
export class UserCredentialsControllerService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `update()` */
  static readonly UpdatePath = '/api/v1/user-credentials/update';

  /**
   * Update user credentials.
   *
   * Updates the user credentials and returns the updated credentials.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `update()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update$Response(params: Update$Params, context?: HttpContext): Observable<StrictHttpResponse<UserCredentialsDto>> {
    return update(this.http, this.rootUrl, params, context);
  }

  /**
   * Update user credentials.
   *
   * Updates the user credentials and returns the updated credentials.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `update$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update(params: Update$Params, context?: HttpContext): Observable<UserCredentialsDto> {
    return this.update$Response(params, context).pipe(
      map((r: StrictHttpResponse<UserCredentialsDto>): UserCredentialsDto => r.body)
    );
  }

  /** Path part for operation `resetPassword()` */
  static readonly ResetPasswordPath = '/api/v1/user-credentials/reset-password';

  /**
   * Reset the user's password using the provided reset token.
   *
   * This method resets the user's password after validating the reset token and setting the new password.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `resetPassword()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  resetPassword$Response(params: ResetPassword$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return resetPassword(this.http, this.rootUrl, params, context);
  }

  /**
   * Reset the user's password using the provided reset token.
   *
   * This method resets the user's password after validating the reset token and setting the new password.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `resetPassword$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  resetPassword(params: ResetPassword$Params, context?: HttpContext): Observable<void> {
    return this.resetPassword$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `register()` */
  static readonly RegisterPath = '/api/v1/user-credentials/register';

  /**
   * Register a new user.
   *
   * Registers a new user with provided credentials and returns an authentication response containing a JWT token.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `register()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  register$Response(params: Register$Params, context?: HttpContext): Observable<StrictHttpResponse<AuthenticationResponseDto>> {
    return register(this.http, this.rootUrl, params, context);
  }

  /**
   * Register a new user.
   *
   * Registers a new user with provided credentials and returns an authentication response containing a JWT token.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `register$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  register(params: Register$Params, context?: HttpContext): Observable<AuthenticationResponseDto> {
    return this.register$Response(params, context).pipe(
      map((r: StrictHttpResponse<AuthenticationResponseDto>): AuthenticationResponseDto => r.body)
    );
  }

  /** Path part for operation `recoverPassword()` */
  static readonly RecoverPasswordPath = '/api/v1/user-credentials/recover-password';

  /**
   * Recover password by sending a reset link to the logged user's email.
   *
   * Sends a password recovery email to the user with a reset token.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `recoverPassword()` instead.
   *
   * This method doesn't expect any request body.
   */
  recoverPassword$Response(params?: RecoverPassword$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return recoverPassword(this.http, this.rootUrl, params, context);
  }

  /**
   * Recover password by sending a reset link to the logged user's email.
   *
   * Sends a password recovery email to the user with a reset token.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `recoverPassword$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  recoverPassword(params?: RecoverPassword$Params, context?: HttpContext): Observable<void> {
    return this.recoverPassword$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `delete()` */
  static readonly DeletePath = '/api/v1/user-credentials/delete';

  /**
   * Delete user credentials.
   *
   * Deletes the provided user credentials from the system.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  delete$Response(params: Delete$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return delete$(this.http, this.rootUrl, params, context);
  }

  /**
   * Delete user credentials.
   *
   * Deletes the provided user credentials from the system.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `delete$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  delete(params: Delete$Params, context?: HttpContext): Observable<void> {
    return this.delete$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
