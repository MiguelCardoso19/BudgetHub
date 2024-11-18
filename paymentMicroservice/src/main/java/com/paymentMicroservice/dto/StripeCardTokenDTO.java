package com.paymentMicroservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StripeCardTokenDTO {

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
