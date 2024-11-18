package com.paymentMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO representing a budget subtype, containing details about the subtype and its associated budget type.")
public class BudgetSubtypeDTO extends AbstractDTO {

    @NotNull
    @NotEmpty
    @Schema(description = "Name of the budget subtype.",
            example = "Marketing",
            required = true)
    private String name;

    @Schema(description = "Available funds for this budget subtype.",
            example = "1200.50",
            nullable = true)
    private Double availableFunds;

    @NotNull
    @NotEmpty
    @Schema(description = "Description of the budget subtype.",
            example = "Budget allocated for marketing activities.",
            required = true)
    private String description;

    @Schema(description = "UUID of the associated budget type.",
            example = "123e4567-e89b-12d3-a456-426614174000",
            nullable = true)
    private UUID budgetTypeId;

    @Schema(description = "Associated BudgetTypeDTO for this budget subtype. It provides additional details about the budget type.",
            nullable = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BudgetTypeDTO budgetType;
}
