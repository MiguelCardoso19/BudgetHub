package com.budgetMicroservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Getter
@Setter
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
}
