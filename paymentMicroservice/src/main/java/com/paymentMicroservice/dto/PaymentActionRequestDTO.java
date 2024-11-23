package com.paymentMicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "PaymentActionRequestDTO", description = "Request object to perform an action on a payment intent")
public class PaymentActionRequestDTO {

    @NotNull
    @Schema(description = "The unique identifier of the payment intent",
            example = "pi_1JN3zd2eZvKYlo2CvZJZ1234",
            required = true)
    private String paymentIntentId;

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