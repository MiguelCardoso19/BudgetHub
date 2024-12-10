/**
 * User credentials including username and password
 */
export interface SignInRequestDto {

  /**
   * Email address of the user
   */
  email: string;

  /**
   * Password of the user, used for authentication
   */
  password: string;
}
