package com.budgetMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BudgetTypeDTO extends AbstractDTO {

    @NotNull
    @NotEmpty
    private String name;

    private Double totalSpent;

    @NotNull
    @NotEmpty
    private String description;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<BudgetSubtypeDTO> subtypes;
}
