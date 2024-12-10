/**
 * Data Transfer Object representing sorting options, including sorting direction and field for a query.
 */
export type SortDto = ({

/**
 * Indicates whether the data is sorted.
 */
'sorted'?: boolean;

/**
 * Indicates whether the data is unsorted.
 */
'unsorted'?: boolean;

/**
 * Indicates whether the data is empty (i.e., no sorting applied).
 */
'empty'?: boolean;

/**
 * The field by which the data should be sorted.
 */
'field': string;

/**
 * The direction of sorting (ASC or DESC).
 */
'direction': 'ASC' | 'DESC';
}) | null;
