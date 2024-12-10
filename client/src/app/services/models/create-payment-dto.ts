import { CreatePaymentItemDto } from '../models/create-payment-item-dto';

/**
 * DTO for creating a new payment
 */
export interface CreatePaymentDto {

  /**
   * Correlation ID for tracking the payment across services
   */
  correlationId?: string | null;

  /**
   * Currency for the payment (e.g., USD, EUR)
   */
  currency: string;

  /**
   * Description of the payment's purpose
   */
  description: string;

  /**
   * Array of items associated with the payment
   */
  items: Array<CreatePaymentItemDto>;

  /**
   * Type of movement associated with the payment
   */
  movementType: 'DEBIT' | 'CREDIT' | 'DEBIT' | 'CREDIT';

  /**
   * Payment method identifier from the payment provider
   */
  paymentMethodId: string;

  /**
   * Email address to send the payment receipt to
   */
  receiptEmail?: string | null;
}
