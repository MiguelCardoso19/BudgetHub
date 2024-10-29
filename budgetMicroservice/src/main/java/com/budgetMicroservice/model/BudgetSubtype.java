package com.budgetMicroservice.model;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
public class BudgetSubtype extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    private Double totalSpent;

    @Column(nullable = false)
    private String description;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "budget_type_id")
    private BudgetType budgetType;

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

    public BudgetType getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(BudgetType budgetType) {
        this.budgetType = budgetType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
