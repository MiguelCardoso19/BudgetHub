package com.paymentMicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
@Schema(name = "CreatePaymentItemDTO", description = "DTO representing a single item in a payment")
public class CreatePaymentItemDTO {

    @Schema(description = "Unique identifier for the payment item", example = "item_12345", nullable = true)
    private String id;

    @Schema(description = "Amount associated with the payment item. Must be at least 1.",
            example = "5000", required = true)
    @NotNull
    @Min(1)
    private Long amount;

    @Schema(description = "Budget Subtype ID associated with the item (if applicable)",
            example = "d290f1ee-6c54-4b01-90e6-d701748f0851", nullable = true)
    private UUID budgetSubtypeId;

    @Schema(description = "Budget Type ID associated with the item (if applicable)",
            example = "c123d456-78e9-0123-4567-890ab123cdef", nullable = true)
    private UUID budgetTypeId;

    @Schema(description = "Supplier ID associated with the item",
            example = "f123a456-78e9-0123-4567-890cd123efab", required = true)
    @NotNull
    private UUID supplierId;

    @Schema(description = "IVA rate applied to the item (default is 0.0)",
            example = "0.23", nullable = true)
    private Double ivaRate = 0.0;
}