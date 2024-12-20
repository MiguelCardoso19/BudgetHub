/**
 * DTO containing details for Stripe SEPA token generation.
 */
export interface StripeSepaTokenDto {

  /**
   * Name of the account holder for the SEPA account
   */
  accountHolderName: string;

  /**
   * Type of the account holder (e.g., individual or company)
   */
  accountHolderType: string;

  /**
   * Correlation ID to track the request
   */
  correlationId?: string | null;

  /**
   * IBAN (International Bank Account Number) associated with the SEPA account
   */
  iban: string;

  /**
   * Indicates whether the SEPA tokenization was successful
   */
  success?: boolean | null;

  /**
   * Token generated by Stripe after successful SEPA account processing
   */
  token?: string | null;
}
