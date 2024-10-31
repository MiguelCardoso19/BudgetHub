package com.budgetMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BudgetSubtypeDTO> getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(List<BudgetSubtypeDTO> subtypes) {
        this.subtypes = subtypes;
    }
}
