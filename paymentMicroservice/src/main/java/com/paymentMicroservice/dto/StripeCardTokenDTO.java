package com.paymentMicroservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class StripeCardTokenDTO {

    private UUID correlationId;

    @NotNull
    private String cardNumber;

    @NotNull
    private String expMonth;

    @NotNull
    private String expYear;

    @NotNull
    private String cvc;

    @NotNull
    private String email;

    private String token;

    private boolean success;
}
