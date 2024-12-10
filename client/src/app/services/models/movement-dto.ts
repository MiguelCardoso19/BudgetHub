import { BudgetSubtypeDto } from '../models/budget-subtype-dto';
import { BudgetTypeDto } from '../models/budget-type-dto';
import { InvoiceDto } from '../models/invoice-dto';
import { SupplierDto } from '../models/supplier-dto';

/**
 * DTO representing a financial movement, including details such as the supplier, type, value, associated invoice, and budget information.
 */
export interface MovementDto {
  budgetSubtype?: BudgetSubtypeDto | null;

  /**
   * UUID of the associated budget subtype, if applicable.
   */
  budgetSubtypeId?: string | null;
  budgetType?: BudgetTypeDto | null;

  /**
   * UUID of the associated budget type, if applicable.
   */
  budgetTypeId?: string | null;

  /**
   * Unique correlation ID to trace and associate requests/responses.
   */
  correlationId: string;

  /**
   * Date of emission for the movement.
   */
  dateOfEmission: string;

  /**
   * Description of the movement.
   */
  description: string;

  /**
   * Document number associated with the movement.
   */
  documentNumber?: string | null;

  /**
   * Unique identifier for the entity, typically assigned upon creation or retrieval.
   */
  id: string;
  invoice?: InvoiceDto | null;

  /**
   * UUID of the associated invoice for the movement, if applicable.
   */
  invoiceId?: string | null;

  /**
   * IVA (VAT) rate applied to the movement, default is 0.0 if not specified.
   */
  ivaRate?: number | null;

  /**
   * Calculated IVA (VAT) value for the movement, based on the IVA rate and value.
   */
  ivaValue?: number | null;

  /**
   * Status of the movement, such as ACCEPTED, PENDING, etc. Default is 'ACCEPTED'.
   */
  status: 'ACCEPTED' | 'PROCESSING' | 'REFUSED' | 'CANCELED' | 'FAILED' | 'REFUNDED' | 'SUCCEEDED';
  supplier?: SupplierDto;

  /**
   * UUID of the supplier associated with the movement.
   */
  supplierId: string;

  /**
   * Total value of the movement, including IVA (VAT).
   */
  totalValue?: number | null;

  /**
   * Type of the movement (e.g., INCOME, EXPENSE).
   */
  type: 'DEBIT' | 'CREDIT';

  /**
   * Value of the movement excluding VAT (IVA).
   */
  valueWithoutIva: number;

  /**
   * Version number for optimistic locking and concurrent updates management.
   */
  version: number;
}
