import { SortDto } from '../models/sort-dto';

/**
 * DTO representing pagination details including page number, page size, offset, sort order, and correlation ID.
 */
export interface CustomPageableDto {

  /**
   * Unique correlation ID to trace and associate requests/responses.
   */
  correlationId: string;

  /**
   * Offset of the requested page in relation to the total data set.
   */
  offset: number;

  /**
   * Page number of the requested data.
   */
  pageNumber: number;

  /**
   * Page size specifying the number of items per page.
   */
  pageSize: number;

  /**
   * Indicates whether pagination is enabled.
   */
  paged: boolean;
  sort?: SortDto | null;

  /**
   * Indicates whether pagination is disabled (for unpaged data requests).
   */
  unpaged: boolean;
}
