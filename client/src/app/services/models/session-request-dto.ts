import { CreatePaymentItemDto } from '../models/create-payment-item-dto';

/**
 * Request object to create a payment session
 */
export interface SessionRequestDto {

  /**
   * Optional correlation ID to track the session
   */
  correlationId?: string | null;

  /**
   * The currency for the payment session
   */
  currency: string;

  /**
   * Description of the payment session
   */
  description: string | null;

  /**
   * Email address of the user initiating the payment session
   */
  email?: string | null;

  /**
   * Array of items included in the payment session
   */
  items: Array<CreatePaymentItemDto>;
}
