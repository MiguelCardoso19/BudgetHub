/**
 * Request object to perform an action on a payment intent
 */
export interface PaymentActionRequestDto {

  /**
   * The unique identifier of the payment intent
   */
  paymentIntentId: string;

  /**
   * The identifier of the payment method used for this transaction
   */
  paymentMethodId?: string | null;

  /**
   * The email address to send the payment receipt to
   */
  receiptEmail: string | null;

  /**
   * The URL to redirect the user after payment is completed
   */
  returnUrl?: string | null;
}
