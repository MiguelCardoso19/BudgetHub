package com.budgetMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceDTO extends AbstractDTO {

    @NotNull
    @NotEmpty
    private LocalDate dateOfEmission;

    @NotNull
    @NotEmpty
    private String documentNumber;

    @NotNull
    @NotEmpty
    private String description;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID movementId;

    private byte[] file;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private MovementDTO movement;

    public MovementDTO getMovement() {
        return movement;
    }

    public void setMovement(MovementDTO movement) {
        this.movement = movement;
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

    public UUID getMovementId() {
        return movementId;
    }

    public void setMovementId(UUID movementId) {
        this.movementId = movementId;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
