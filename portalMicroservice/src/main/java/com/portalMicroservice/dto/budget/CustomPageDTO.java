package com.portalMicroservice.dto.budget;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "DTO representing a paginated response, containing a list of content items along with pagination details.")
public class CustomPageDTO<T> {

    @Schema(description = "List of content items for the current page.",
            required = true)
    private List<T> content;

    @Schema(description = "The number of items per page.",
            example = "20", required = true)
    private int pageSize;

    @Schema(description = "Total number of elements in the entire data set.",
            example = "1000", required = true)
    private long totalElements;

    @Schema(description = "Total number of pages in the data set.",
            example = "50", required = true)
    private int totalPages;

    @Schema(description = "Pagination details including page number, page size, offset, and sort order.",
            required = true)
    private CustomPageableDTO pageable;

    @Schema(description = "Indicates whether the current page is the last page.",
            example = "false", required = true)
    private boolean last;

    @Schema(description = "Indicates whether the current page is the first page.",
            example = "true", required = true)
    private boolean first;

    @Schema(description = "The number of elements in the current page.",
            example = "20", required = true)
    private int numberOfElements;

    @Schema(description = "Indicates whether the current page contains any content.",
            example = "false", required = true)
    private boolean empty;

    public CustomPageDTO(List<T> content, int pageSize, long totalElements,
                         CustomPageableDTO pageable, boolean last, boolean first,
                         int numberOfElements, boolean empty) {
        this.content = content;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        this.pageable = pageable;
        this.last = last;
        this.first = first;
        this.numberOfElements = numberOfElements;
        this.empty = empty;
    }
}
