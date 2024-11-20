package com.portalMicroservice.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CreatePaymentResponseDTO {
    private String paymentIntentId;
    private UUID correlationId;
}