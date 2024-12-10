import { BudgetSubtypeDto } from '../models/budget-subtype-dto';

/**
 * DTO representing a budget type, including its name, description, total spent, and associated subtypes.
 */
export type BudgetTypeDto = {

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
 * Name of the budget type.
 */
'name': string;

/**
 * Total amount spent for this budget type.
 */
'availableFunds'?: number | null;

/**
 * Description of the budget type.
 */
'description': string;

/**
 * List of associated budget subtypes for this budget type.
 */
'subtypes'?: Array<BudgetSubtypeDto | null>;
};
