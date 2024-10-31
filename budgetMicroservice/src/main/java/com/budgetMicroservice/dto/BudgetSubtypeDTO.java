package com.budgetMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class BudgetSubtypeDTO extends AbstractDTO {

    @NotNull
    @NotEmpty
    private String name;

    private Double totalSpent;

    @NotNull
    @NotEmpty
    private String description;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID budgetTypeId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BudgetTypeDTO budgetType;

    public BudgetTypeDTO getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(BudgetTypeDTO budgetType) {
        this.budgetType = budgetType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(Double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public UUID getBudgetTypeId() {
        return budgetTypeId;
    }

    public void setBudgetTypeId(UUID budgetTypeId) {
        this.budgetTypeId = budgetTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
