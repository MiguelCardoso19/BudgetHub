package com.budgetMicroservice.model;

import com.budgetMicroservice.enumerator.MovementStatus;
import com.budgetMicroservice.enumerator.MovementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDate;

@Entity
@Audited
@Getter
@Setter
public class Movement extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType movementType;

    @Column(nullable = false)
    private LocalDate dateOfEmission;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double valueWithoutIva;

    private Double ivaValue;

    private Double ivaRate;

    private Double totalValue;

    @Enumerated(EnumType.STRING)
    private MovementStatus status;

    @NotAudited
    @OneToOne(mappedBy = "movement", cascade = CascadeType.ALL)
    private Invoice invoice;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "budget_subtype_id")
    private BudgetSubtype budgetSubtype;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "budget_type_id")
    private BudgetType budgetType;

    @NotAudited
    @ManyToOne
    private Supplier supplier;
}
