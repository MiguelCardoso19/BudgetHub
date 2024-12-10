import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { cancelPayment } from '../fn/payment-controller/cancel-payment';
import { CancelPayment$Params } from '../fn/payment-controller/cancel-payment';
import { confirmPayment } from '../fn/payment-controller/confirm-payment';
import { ConfirmPayment$Params } from '../fn/payment-controller/confirm-payment';
import { createCardToken } from '../fn/payment-controller/create-card-token';
import { CreateCardToken$Params } from '../fn/payment-controller/create-card-token';
import { createPaymentIntent } from '../fn/payment-controller/create-payment-intent';
import { CreatePaymentIntent$Params } from '../fn/payment-controller/create-payment-intent';
import { CreatePaymentResponseDto } from '../models/create-payment-response-dto';
import { createPaymentSession } from '../fn/payment-controller/create-payment-session';
import { CreatePaymentSession$Params } from '../fn/payment-controller/create-payment-session';
import { createSepaToken } from '../fn/payment-controller/create-sepa-token';
import { CreateSepaToken$Params } from '../fn/payment-controller/create-sepa-token';
import { refundCharge } from '../fn/payment-controller/refund-charge';
import { RefundCharge$Params } from '../fn/payment-controller/refund-charge';
import { SessionResponseDto } from '../models/session-response-dto';
import { StripeCardTokenDto } from '../models/stripe-card-token-dto';
import { StripeSepaTokenDto } from '../models/stripe-sepa-token-dto';


/**
 * Operations related to Payments
 */
@Injectable({ providedIn: 'root' })
export class PaymentControllerService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `refundCharge()` */
  static readonly RefundChargePath = '/api/v1/payment/refund';

  /**
   * Refund Charge.
   *
   * Initiates a refund for a charge based on the provided refund request.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `refundCharge()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  refundCharge$Response(params: RefundCharge$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return refundCharge(this.http, this.rootUrl, params, context);
  }

  /**
   * Refund Charge.
   *
   * Initiates a refund for a charge based on the provided refund request.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `refundCharge$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  refundCharge(params: RefundCharge$Params, context?: HttpContext): Observable<string> {
    return this.refundCharge$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `createSepaToken()` */
  static readonly CreateSepaTokenPath = '/api/v1/payment/create-sepa-token';

  /**
   * Create SEPA Token.
   *
   * Creates a Stripe SEPA token from the provided bank account details.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createSepaToken()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createSepaToken$Response(params: CreateSepaToken$Params, context?: HttpContext): Observable<StrictHttpResponse<StripeSepaTokenDto>> {
    return createSepaToken(this.http, this.rootUrl, params, context);
  }

  /**
   * Create SEPA Token.
   *
   * Creates a Stripe SEPA token from the provided bank account details.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createSepaToken$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createSepaToken(params: CreateSepaToken$Params, context?: HttpContext): Observable<StripeSepaTokenDto> {
    return this.createSepaToken$Response(params, context).pipe(
      map((r: StrictHttpResponse<StripeSepaTokenDto>): StripeSepaTokenDto => r.body)
    );
  }

  /** Path part for operation `createPaymentSession()` */
  static readonly CreatePaymentSessionPath = '/api/v1/payment/create-payment-session';

  /**
   * Create Payment Session.
   *
   * Creates a payment session to handle the payment process.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createPaymentSession()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createPaymentSession$Response(params: CreatePaymentSession$Params, context?: HttpContext): Observable<StrictHttpResponse<SessionResponseDto>> {
    return createPaymentSession(this.http, this.rootUrl, params, context);
  }

  /**
   * Create Payment Session.
   *
   * Creates a payment session to handle the payment process.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createPaymentSession$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createPaymentSession(params: CreatePaymentSession$Params, context?: HttpContext): Observable<SessionResponseDto> {
    return this.createPaymentSession$Response(params, context).pipe(
      map((r: StrictHttpResponse<SessionResponseDto>): SessionResponseDto => r.body)
    );
  }

  /** Path part for operation `createPaymentIntent()` */
  static readonly CreatePaymentIntentPath = '/api/v1/payment/create-payment-intent';

  /**
   * Create a Payment Intent.
   *
   * Creates a payment intent based on the provided payment details. The payment intent is a payment request that can later be confirmed or canceled.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createPaymentIntent()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createPaymentIntent$Response(params: CreatePaymentIntent$Params, context?: HttpContext): Observable<StrictHttpResponse<CreatePaymentResponseDto>> {
    return createPaymentIntent(this.http, this.rootUrl, params, context);
  }

  /**
   * Create a Payment Intent.
   *
   * Creates a payment intent based on the provided payment details. The payment intent is a payment request that can later be confirmed or canceled.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createPaymentIntent$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createPaymentIntent(params: CreatePaymentIntent$Params, context?: HttpContext): Observable<CreatePaymentResponseDto> {
    return this.createPaymentIntent$Response(params, context).pipe(
      map((r: StrictHttpResponse<CreatePaymentResponseDto>): CreatePaymentResponseDto => r.body)
    );
  }

  /** Path part for operation `createCardToken()` */
  static readonly CreateCardTokenPath = '/api/v1/payment/create-card-token';

  /**
   * Create Card Token.
   *
   * Creates a Stripe card token from the provided card details.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createCardToken()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createCardToken$Response(params: CreateCardToken$Params, context?: HttpContext): Observable<StrictHttpResponse<StripeCardTokenDto>> {
    return createCardToken(this.http, this.rootUrl, params, context);
  }

  /**
   * Create Card Token.
   *
   * Creates a Stripe card token from the provided card details.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createCardToken$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createCardToken(params: CreateCardToken$Params, context?: HttpContext): Observable<StripeCardTokenDto> {
    return this.createCardToken$Response(params, context).pipe(
      map((r: StrictHttpResponse<StripeCardTokenDto>): StripeCardTokenDto => r.body)
    );
  }

  /** Path part for operation `confirmPayment()` */
  static readonly ConfirmPaymentPath = '/api/v1/payment/confirm';

  /**
   * Confirm Payment.
   *
   * Confirms a pending payment based on the provided payment action request.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `confirmPayment()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  confirmPayment$Response(params: ConfirmPayment$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return confirmPayment(this.http, this.rootUrl, params, context);
  }

  /**
   * Confirm Payment.
   *
   * Confirms a pending payment based on the provided payment action request.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `confirmPayment$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  confirmPayment(params: ConfirmPayment$Params, context?: HttpContext): Observable<string> {
    return this.confirmPayment$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `cancelPayment()` */
  static readonly CancelPaymentPath = '/api/v1/payment/cancel';

  /**
   * Cancel Payment.
   *
   * Cancels a pending payment based on the provided payment action request.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `cancelPayment()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  cancelPayment$Response(params: CancelPayment$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return cancelPayment(this.http, this.rootUrl, params, context);
  }

  /**
   * Cancel Payment.
   *
   * Cancels a pending payment based on the provided payment action request.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `cancelPayment$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  cancelPayment(params: CancelPayment$Params, context?: HttpContext): Observable<void> {
    return this.cancelPayment$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
