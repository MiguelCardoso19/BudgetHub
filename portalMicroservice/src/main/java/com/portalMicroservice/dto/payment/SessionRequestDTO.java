package com.portalMicroservice.dto.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class SessionRequestDTO {

    @NotNull
    String currency;

    private UUID correlationId;

    private String email;

    @NotNull
    private String description;

    @NotNull
    CreatePaymentItemDTO[] items;
}