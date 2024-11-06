package com.portalMicroservice.dto.budget;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BudgetSubtypeDTO extends AbstractDTO {

    @NotNull
    @NotEmpty
    private String name;

    private Double totalSpent;

    @NotNull
    @NotEmpty
    private String description;

    private UUID budgetTypeId;

    private BudgetTypeDTO budgetType;
}
