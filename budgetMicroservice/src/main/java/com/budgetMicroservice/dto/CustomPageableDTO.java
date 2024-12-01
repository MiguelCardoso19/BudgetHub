package com.budgetMicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing pagination details including page number, page size, offset, sort order, and correlation ID.")
public class CustomPageableDTO {

    @Schema(description = "Unique correlation ID to trace and associate requests/responses.",
            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479", required = true)
    private UUID correlationId;

    @Schema(description = "Page number of the requested data.",
            example = "1", required = true)
    private int pageNumber;

    @Schema(description = "Page size specifying the number of items per page.",
            example = "20", required = true)
    private int pageSize;

    @Schema(description = "Offset of the requested page in relation to the total data set.",
            example = "0", required = true)
    private int offset;

    @Schema(description = "Indicates whether pagination is enabled.",
            example = "true", required = true)
    private boolean paged;

    @Schema(description = "Indicates whether pagination is disabled (for unpaged data requests).",
            example = "false", required = true)
    private boolean unpaged;

    @Schema(description = "Sort information for ordering the results, containing the field and direction.",
            nullable = true)
    private SortDTO sort;

    @Schema(description = "Sort field extracted from the sort DTO.",
            example = "name", required = false)
    public String getSortField() {
        return sort != null ? sort.getField() : null;
    }

    @Schema(description = "Sort order (ASC or DESC) extracted from the sort DTO. Defaults to 'ASC' if not provided.",
            example = "ASC", required = false)
    public String getSortOrder() {
        return sort != null && sort.getDirection() != null ? sort.getDirection().name() : "ASC";
    }
}
