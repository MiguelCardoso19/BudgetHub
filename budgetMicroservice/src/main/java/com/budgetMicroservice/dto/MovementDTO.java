package com.budgetMicroservice.dto;

import com.budgetMicroservice.enumerator.MovementType;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    private UUID supplierId;

    @NotNull
    @NotEmpty
    private UUID typeId;

    private UUID subtypeId;

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

    private Double ivaValue;

    @NotNull
    @NotEmpty
    private Double totalValue;

    private boolean paid;

    public UUID getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    public UUID getTypeId() {
        return typeId;
    }

    public void setTypeId(UUID typeId) {
        this.typeId = typeId;
    }

    public UUID getSubtypeId() {
        return subtypeId;
    }

    public void setSubtypeId(UUID subtypeId) {
        this.subtypeId = subtypeId;
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