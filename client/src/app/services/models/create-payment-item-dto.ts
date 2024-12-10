/**
 * DTO representing a single item in a payment
 */
export interface CreatePaymentItemDto {

  /**
   * Amount associated with the payment item. Must be at least 1.
   */
  amount: number;

  /**
   * Budget Subtype ID associated with the item (if applicable)
   */
  budgetSubtypeId?: string | null;

  /**
   * Budget Type ID associated with the item (if applicable)
   */
  budgetTypeId?: string | null;

  /**
   * Unique identifier for the payment item
   */
  id?: string | null;

  /**
   * IVA rate applied to the item (default is 0.0)
   */
  ivaRate?: number | null;

  /**
   * Supplier ID associated with the item
   */
  supplierId: string;
}
