/**
 * User credentials to delete
 */
export interface DeleteRequestDto {

  /**
   * Unique identifier (UUID) for each entity instance
   */
  id?: string;

  /**
   * Password of the user, required to authorize deletion
   */
  password: string;
}
