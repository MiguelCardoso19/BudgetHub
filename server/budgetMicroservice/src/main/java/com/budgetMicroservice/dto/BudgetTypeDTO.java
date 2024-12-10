package com.budgetMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO representing a budget type, including its name, description, total spent, and associated subtypes.")
public class BudgetTypeDTO extends AbstractDTO {

    @NotNull
    @NotEmpty
    @Schema(description = "Name of the budget type.",
            example = "Operational",
            required = true)
    private String name;

    @Schema(description = "Available funds for this budget type.",
            example = "1200.50",
            nullable = true)
    private Double availableFunds;

    @NotNull
    @NotEmpty
    @Schema(description = "Description of the budget type.",
            example = "Budget for operational expenses.",
            required = true)
    private String description;

    @Schema(description = "List of associated budget subtypes for this budget type.",
            nullable = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<BudgetSubtypeDTO> subtypes;
}
