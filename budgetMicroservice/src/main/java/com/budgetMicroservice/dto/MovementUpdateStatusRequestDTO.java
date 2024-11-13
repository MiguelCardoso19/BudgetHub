package com.budgetMicroservice.dto;

import com.budgetMicroservice.enumerator.MovementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "DTO representing a request to update the status of a movement.")
public class MovementUpdateStatusRequestDTO extends AbstractDTO {

    @Enumerated(EnumType.STRING)
    @Schema(description = "The new status to update the movement to.",
            example = "PAID", required = true)
    private MovementStatus status;
}