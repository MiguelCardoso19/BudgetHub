import { BudgetTypeDto } from '../models/budget-type-dto';

/**
 * DTO representing a budget subtype, containing details about the subtype and its associated budget type.
 */
export type BudgetSubtypeDto = {

/**
 * Unique correlation ID to trace and associate requests/responses.
 */
'correlationId': string;

/**
 * Unique identifier for the entity, typically assigned upon creation or retrieval.
 */
'id': string;

/**
 * Version number for optimistic locking and concurrent updates management.
 */
'version': number;

/**
 * Name of the budget subtype.
 */
'name': string;

/**
 * Total amount spent for this budget subtype.
 */
'availableFunds'?: number | null;

/**
 * Description of the budget subtype.
 */
'description': string;

/**
 * UUID of the associated budget type.
 */
'budgetTypeId'?: string | null;
'budgetType'?: BudgetTypeDto | null;
};
