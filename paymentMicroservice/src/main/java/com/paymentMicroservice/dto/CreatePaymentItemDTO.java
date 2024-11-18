package com.paymentMicroservice.dto;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreatePaymentItemDTO {

    @SerializedName("id")
    String id;

    @NotNull
    @SerializedName("amount")
    @Min(1)
    Long amount;


    UUID budgetSubtypeId;

    UUID budgetTypeId;

    @NotNull
    UUID supplierId;

    Double ivaRate = 0.0;
}