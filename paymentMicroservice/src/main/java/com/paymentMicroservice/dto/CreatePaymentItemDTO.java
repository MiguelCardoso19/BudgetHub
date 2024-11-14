package com.paymentMicroservice.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class CreatePaymentItemDTO {

    @SerializedName("id")
    String id;

    @SerializedName("amount")
    Long amount;
}