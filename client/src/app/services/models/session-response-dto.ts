/**
 * Response containing session ID, URL, and correlation ID.
 */
export interface SessionResponseDto {

  /**
   * Correlation ID to track the session
   */
  correlationId?: string | null;

  /**
   * Unique identifier for the payment session
   */
  sessionId: string;

  /**
   * URL to the payment session
   */
  sessionUrl: string;
}
