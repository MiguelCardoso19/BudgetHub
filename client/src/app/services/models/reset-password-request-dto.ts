/**
 * Reset password request containing the reset token and the new password
 */
export interface ResetPasswordRequestDto {

  /**
   * The new password to set for the user.
   */
  newPassword: string;

  /**
   * The password reset token, usually sent via email for validation.
   */
  token: string;
}
