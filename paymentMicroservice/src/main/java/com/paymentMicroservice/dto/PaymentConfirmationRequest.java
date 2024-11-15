package com.paymentMicroservice.dto;

import lombok.Data;

@Data
public class PaymentConfirmationRequest {

    private String clientSecret;
    private String receiptEmail;
    private String returnUrl;
    private String paymentMethodId;
}
