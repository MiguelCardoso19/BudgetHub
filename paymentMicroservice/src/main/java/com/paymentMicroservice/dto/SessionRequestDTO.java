package com.paymentMicroservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class SessionRequestDTO {

    @NotNull
    String currency;

    private UUID correlationId;

    private String email;

    private String description;

    @NotNull
    CreatePaymentItemDTO[] items;
}