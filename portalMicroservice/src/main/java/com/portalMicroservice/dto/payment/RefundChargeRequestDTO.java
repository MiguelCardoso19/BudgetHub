package com.portalMicroservice.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "RefundChargeRequestDTO", description = "Request object to initiate a refund for a payment intent")
public class RefundChargeRequestDTO {

    @NotNull
    @NotEmpty
    @Schema(description = "The unique identifier of the payment intent to be refunded",
            example = "pi_1JN3zd2eZvKYlo2CvZJZ1234",
            required = true)
    private String paymentIntentId;
}