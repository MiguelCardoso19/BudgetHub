package com.paymentMicroservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefundChargeRequestDTO {

    @NotNull
    String paymentIntentId;
}