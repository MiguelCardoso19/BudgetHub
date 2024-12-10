/**
 * Request object to initiate a refund for a payment intent
 */
export interface RefundChargeRequestDto {

  /**
   * The unique identifier of the payment intent to be refunded
   */
  paymentIntentId: string;
}
