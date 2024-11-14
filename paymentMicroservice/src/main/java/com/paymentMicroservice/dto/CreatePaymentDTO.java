package com.paymentMicroservice.dto;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class CreatePaymentDTO {
    @SerializedName("items")
    CreatePaymentItemDTO[] items;
}
