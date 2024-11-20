package com.portalMicroservice.dto.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefundChargeRequestDTO {

    @NotNull
    String paymentIntentId;
}