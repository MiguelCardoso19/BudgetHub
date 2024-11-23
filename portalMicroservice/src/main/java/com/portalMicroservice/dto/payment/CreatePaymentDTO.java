package com.portalMicroservice.dto.payment;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.portalMicroservice.enumerator.MovementType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "CreatePaymentDTO", description = "DTO for creating a new payment")
public class CreatePaymentDTO {

    @Schema(description = "Currency for the payment (e.g., USD, EUR)", example = "USD", required = true)
    @NotNull
    @NotEmpty
    private String currency;

    @Schema(description = "Payment method identifier from the payment provider", example = "pm_1Jgghy2eZvKYlo2CQ0rZdmKZ", required = true)
    @NotNull
    @NotEmpty
    private String paymentMethodId;

    @Schema(description = "Email address to send the payment receipt to", example = "customer@example.com", nullable = true)
    @Email
    private String receiptEmail;

    @Schema(description = "Description of the payment's purpose", example = "Payment for invoice #12345", required = true)
    @NotNull
    @NotEmpty
    private String description;

    @Schema(description = "Array of items associated with the payment", required = true)
    @NotNull
    @NotEmpty
    private CreatePaymentItemDTO[] items;

    @Schema(description = "Type of movement associated with the payment",
            example = "EXPENSE",
            required = true,
            allowableValues = {"DEBIT", "CREDIT"})
    @NotNull
    @NotEmpty
    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    @Schema(description = "Correlation ID for tracking the payment across services", example = "d290f1ee-6c54-4b01-90e6-d701748f0851", nullable = true)
    private UUID correlationId;
}