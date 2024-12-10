/**
 * Data Transfer Object representing a supplier, containing details about the company, responsible person, and contact information.
 */
export interface SupplierDto {

  /**
   * The name of the supplier company.
   */
  companyName: string;

  /**
   * Unique correlation ID to trace and associate requests/responses.
   */
  correlationId: string;

  /**
   * The email address of the supplier.
   */
  email?: string;

  /**
   * Unique identifier for the entity, typically assigned upon creation or retrieval.
   */
  id: string;

  /**
   * The NIF (Tax Identification Number) of the supplier.
   */
  nif: string;

  /**
   * The phone number of the supplier.
   */
  phoneNumber?: string;

  /**
   * The name of the person responsible for the supplier.
   */
  responsibleName: string;

  /**
   * Version number for optimistic locking and concurrent updates management.
   */
  version: number;
}
