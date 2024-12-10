package com.portalMicroservice.dto.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "PaymentActionRequestDTO", description = "Request object to perform an action on a payment intent")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentActionRequestDTO {

    @NotNull
    @NotEmpty
    @Schema(description = "The unique identifier of the payment intent",
            example = "pi_1JN3zd2eZvKYlo2CvZJZ1234",
            required = true)
    private String paymentIntentId;

    @NotNull
    @NotEmpty
    @Schema(description = "The email address to send the payment receipt to",
            example = "customer@example.com",
            nullable = true)
    private String receiptEmail;

    @Schema(description = "The URL to redirect the user after payment is completed",
            example = "https://example.com/return",
            nullable = true)
    private String returnUrl;

    @Schema(description = "The identifier of the payment method used for this transaction",
            example = "pm_1JN3zd2eZvKYlo2CvXAb1234",
            nullable = true)
    private String paymentMethodId;
}