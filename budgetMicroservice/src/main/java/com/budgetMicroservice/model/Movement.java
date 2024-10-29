package com.budgetMicroservice.model;

import com.budgetMicroservice.enumerator.MovementType;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDate;

@Entity
@Audited
public class Movement extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType movementType;

    @Column(nullable = false)
    private LocalDate dateOfEmission;

    @Column(nullable = false)
    private String documentNumber;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double valueWithoutIva;

    private Double ivaValue;

    private Double totalValueWithIva;

    private boolean paid;

    @NotAudited
    @OneToOne(mappedBy = "movement", cascade = CascadeType.ALL)
    private Invoice invoice;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "budget_subtype_id")
    private BudgetSubtype budgetSubtype;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "budget_type_id", nullable = false)
    private BudgetType budgetType;

    @NotAudited
    @ManyToOne
    private Supplier supplier;

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public LocalDate getDateOfEmission() {
        return dateOfEmission;
    }

    public void setDateOfEmission(LocalDate dateOfEmission) {
        this.dateOfEmission = dateOfEmission;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValueWithoutIva() {
        return valueWithoutIva;
    }

    public void setValueWithoutIva(Double valueWithoutIva) {
        this.valueWithoutIva = valueWithoutIva;
    }

    public Double getIvaValue() {
        return ivaValue;
    }

    public void setIvaValue(Double ivaValue) {
        this.ivaValue = ivaValue;
    }

    public Double getTotalValueWithIva() {
        return totalValueWithIva;
    }

    public void setTotalValueWithIva(Double totalValueWithIva) {
        this.totalValueWithIva = totalValueWithIva;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public BudgetSubtype getBudgetSubtype() {
        return budgetSubtype;
    }

    public void setBudgetSubtype(BudgetSubtype budgetSubtype) {
        this.budgetSubtype = budgetSubtype;
    }

    public BudgetType getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(BudgetType budgetType) {
        this.budgetType = budgetType;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
