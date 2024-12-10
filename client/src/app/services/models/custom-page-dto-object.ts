import { CustomPageableDto } from '../models/custom-pageable-dto';

/**
 * DTO representing a paginated response, containing a list of content items along with pagination details.
 */
export interface CustomPageDtoObject {

  /**
   * List of content items for the current page.
   */
  content: Array<{
}>;

  /**
   * Indicates whether the current page contains any content.
   */
  empty: boolean;

  /**
   * Indicates whether the current page is the first page.
   */
  first: boolean;

  /**
   * Indicates whether the current page is the last page.
   */
  last: boolean;

  /**
   * The number of elements in the current page.
   */
  numberOfElements: number;

  /**
   * The number of items per page.
   */
  pageSize: number;
  pageable: CustomPageableDto;

  /**
   * Total number of elements in the entire data set.
   */
  totalElements: number;

  /**
   * Total number of pages in the data set.
   */
  totalPages: number;
}
