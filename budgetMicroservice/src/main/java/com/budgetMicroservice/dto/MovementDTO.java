package com.budgetMicroservice.dto;

import com.budgetMicroservice.enumerator.MovementStatus;
import com.budgetMicroservice.enumerator.MovementType;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

import static com.budgetMicroservice.enumerator.MovementStatus.ACCEPTED;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO representing a financial movement, including details such as the supplier, type, value, associated invoice, and budget information.")
public class MovementDTO extends AbstractDTO {

    @NotNull
    @NotEmpty
    @Schema(description = "UUID of the supplier associated with the movement.",
            example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID supplierId;

    @Schema(description = "UUID of the associated budget type, if applicable.",
            example = "987e6543-e89b-12d3-a456-426614174111", nullable = true)
    private UUID budgetTypeId;

    @Schema(description = "UUID of the associated budget subtype, if applicable.",
            example = "987e6543-e89b-12d3-a456-426614174222", nullable = true)
    private UUID budgetSubtypeId;

    @Schema(description = "UUID of the associated invoice for the movement, if applicable.",
            example = "a1f8b87f-e8c9-4702-92d4-c60ff15c99ab", nullable = true)
    private UUID invoiceId;

    @Schema(description = "Document number associated with the movement.",
            example = "INV-2024-0001", nullable = true)
    private String documentNumber;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Type of the movement (e.g., INCOME, EXPENSE).",
            example = "EXPENSE", required = true)
    private MovementType type;

    @NotNull
    @NotEmpty
    @Schema(description = "Date of emission for the movement.",
            example = "2024-11-10", required = true)
    private LocalDate dateOfEmission;

    @NotNull
    @NotEmpty
    @Schema(description = "Description of the movement.",
            example = "Payment for marketing services", required = true)
    private String description;

    @NotNull
    @NotEmpty
    @Schema(description = "Value of the movement excluding VAT (IVA).",
            example = "1000.00", required = true)
    private Double valueWithoutIva;

    @Schema(description = "IVA (VAT) rate applied to the movement, default is 0.0 if not specified.",
            example = "0.21", nullable = true)
    private Double ivaRate = 0.0;

    @Schema(description = "Calculated IVA (VAT) value for the movement, based on the IVA rate and value.",
            example = "210.00", nullable = true)
    private Double ivaValue;

    @Schema(description = "Total value of the movement, including IVA (VAT).",
            example = "1210.00", nullable = true)
    private Double totalValue;

    @JsonProperty("status")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Status of the movement, such as ACCEPTED, PENDING, etc. Default is 'ACCEPTED'.",
            example = "ACCEPTED", required = true)
    private MovementStatus status = ACCEPTED;

    @Schema(description = "Invoice details associated with the movement, if available.",
            nullable = true)
    private InvoiceDTO invoice;

    @Schema(description = "Budget type details associated with the movement, if available.",
            nullable = true)
    private BudgetTypeDTO budgetType;

    @Schema(description = "Budget subtype details associated with the movement, if available.",
            nullable = true)
    private BudgetSubtypeDTO budgetSubtype;

    @Schema(description = "Supplier details associated with the movement.",
            nullable = true)
    private SupplierDTO supplier;
}