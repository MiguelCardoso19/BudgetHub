package com.paymentMicroservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class StripeSepaTokenDTO {

    private UUID correlationId;

    @NotNull
    private String iban;

    @NotNull
    private String accountHolderName;

    @NotNull
    private String accountHolderType;

    private boolean success;

    private String token;
}
