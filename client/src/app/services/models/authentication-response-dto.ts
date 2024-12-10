/**
 * Data Transfer Object representing the authentication response, including the JWT token, refresh token and user ID.
 */
export interface AuthenticationResponseDto {

  /**
   * Unique identifier for the user
   */
  id?: string;

  /**
   * Refresh token used for obtaining a new JWT token
   */
  refreshToken?: string;

  /**
   * JWT token used for authenticating API requests
   */
  token?: string;
}
