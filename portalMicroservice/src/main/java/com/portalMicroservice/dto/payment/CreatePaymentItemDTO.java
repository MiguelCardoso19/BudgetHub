package com.portalMicroservice.dto.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreatePaymentItemDTO {

    String id;

    @NotNull
    @Min(1)
    Long amount;

    UUID budgetSubtypeId;

    UUID budgetTypeId;

    @NotNull
    UUID supplierId;

    Double ivaRate = 0.0;
}