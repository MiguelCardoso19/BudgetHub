package com.budgetMicroservice.dto;

import com.budgetMicroservice.enumerator.MovementType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovementDTO extends AbstractDTO {

    @NotNull
    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID supplierId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID budgetTypeId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID budgetSubtypeId;

    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    @NotNull
    @NotEmpty
    private LocalDate dateOfEmission;

    @NotNull
    @NotEmpty
    private String documentNumber;

    @NotNull
    @NotEmpty
    private String description;

    @NotNull
    @NotEmpty
    private Double valueWithoutIva;

    private Double ivaRate = 0.0;

    private Double ivaValue;

    private Double totalValue;

    private boolean paid;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private InvoiceDTO invoice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BudgetTypeDTO budgetType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BudgetSubtypeDTO budgetSubtype;

    private SupplierDTO supplier;

    public InvoiceDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
    }

    public BudgetTypeDTO getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(BudgetTypeDTO budgetType) {
        this.budgetType = budgetType;
    }

    public BudgetSubtypeDTO getBudgetSubtype() {
        return budgetSubtype;
    }

    public void setBudgetSubtype(BudgetSubtypeDTO budgetSubtype) {
        this.budgetSubtype = budgetSubtype;
    }

    public SupplierDTO getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierDTO supplier) {
        this.supplier = supplier;
    }

    public Double getIvaRate() {
        return ivaRate;
    }

    public void setIvaRate(Double ivaRate) {
        this.ivaRate = ivaRate;
    }

    public UUID getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    public UUID getBudgetTypeId() {
        return budgetTypeId;
    }

    public void setBudgetTypeId(UUID budgetTypeId) {
        this.budgetTypeId = budgetTypeId;
    }

    public UUID getBudgetSubtypeId() {
        return budgetSubtypeId;
    }

    public void setBudgetSubtypeId(UUID budgetSubtypeId) {
        this.budgetSubtypeId = budgetSubtypeId;
    }

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

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

}