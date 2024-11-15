package com.paymentMicroservice.dto;


import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreatePaymentDTO {

    String currency;

    String paymentMethodId;

    String receiptEmail;

    @NotNull
    @SerializedName("items")
    CreatePaymentItemDTO[] items;
}