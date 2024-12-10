package com.budgetMicroservice.dto;

import com.budgetMicroservice.enumerator.SortDirection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object representing sorting options, including sorting direction and field for a query.")
public class SortDTO {

    @Schema(description = "Indicates whether the data is sorted.", example = "true")
    private boolean sorted;

    @Schema(description = "Indicates whether the data is unsorted.", example = "false")
    private boolean unsorted;

    @Schema(description = "Indicates whether the data is empty (i.e., no sorting applied).", example = "false")
    private boolean empty;

    @Schema(description = "The field by which the data should be sorted.",
            example = "dateOfEmission", required = true)
    private String field;

    @Schema(description = "The direction of sorting (ASC or DESC).",
            example = "ASC", required = true)
    private SortDirection direction;
}
