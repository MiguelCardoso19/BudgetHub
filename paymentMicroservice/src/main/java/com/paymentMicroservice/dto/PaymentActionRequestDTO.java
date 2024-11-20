package com.paymentMicroservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentActionRequestDTO {
    @NotNull
    private String paymentIntentId;

    private String receiptEmail;
    private String returnUrl;
    private String paymentMethodId;
}
