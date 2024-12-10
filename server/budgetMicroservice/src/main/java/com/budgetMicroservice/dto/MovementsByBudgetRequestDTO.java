package com.budgetMicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO representing a request to fetch movements by budget, including a budget ID and pagination details.")
public class MovementsByBudgetRequestDTO {

    @Schema(description = "Correlation ID to track the request.",
            example = "123e4567-e89b-12d3-a456-426614174001")
    private UUID correlationId;

    @Schema(description = "UUID of the budget for which the movements are being requested.",
            example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID id;

    @Schema(description = "Pagination and sorting information for the request.",
            required = true)
    private CustomPageableDTO pageable;
}