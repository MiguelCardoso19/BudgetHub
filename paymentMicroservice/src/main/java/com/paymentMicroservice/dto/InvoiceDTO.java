package com.paymentMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO representing an invoice, including details such as the date of emission, description, associated movement, and file data.")
public class InvoiceDTO extends AbstractDTO {

    @NotNull
    @NotEmpty
    @Schema(description = "The date when the invoice was emitted.",
            example = "2024-11-10", required = true)
    private LocalDate dateOfEmission;

    @NotNull
    @NotEmpty
    @Schema(description = "A brief description of the invoice.",
            example = "Invoice for marketing services", required = true)
    private String description;

    @Schema(description = "UUID of the associated movement, if any.",
            example = "123e4567-e89b-12d3-a456-426614174000", nullable = true)
    private UUID movementId;

    @Lob
    @Schema(description = "The file content of the invoice, stored as a byte array.",
            nullable = true)
    private byte[] file;

    @Schema(description = "The associated movement details for this invoice, if available.",
            nullable = true)
    private MovementDTO movement;
}
