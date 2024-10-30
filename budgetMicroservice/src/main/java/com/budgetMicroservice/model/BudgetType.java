package com.budgetMicroservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.List;

@Entity
@Audited
public class BudgetType extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    private Double totalSpent;

    @Column(nullable = false)
    private String description;

    @NotAudited
    @OneToMany(mappedBy = "budgetType", cascade = CascadeType.ALL)
    private List<BudgetSubtype> subtypes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalSpent() {
        return totalSpent == null ? 0.0 : totalSpent;
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

    public List<BudgetSubtype> getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(List<BudgetSubtype> subtypes) {
        this.subtypes = subtypes;
    }
}
