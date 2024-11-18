package com.paymentMicroservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StripeSepaTokenDTO {

    @NotNull
    private String iban;

    @NotNull
    private String accountHolderName;

    @NotNull
    private String accountHolderType;

    private boolean success;

    private String token;
}
