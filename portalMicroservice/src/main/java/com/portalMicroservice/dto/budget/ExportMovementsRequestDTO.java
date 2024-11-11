package com.portalMicroservice.dto.budget;

import com.portalMicroservice.enumerator.MovementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a request to export movement data for a specific date range and status, associated with a user.")
public class ExportMovementsRequestDTO {

    @Schema(description = "Correlation ID to track the request.",
            example = "123e4567-e89b-12d3-a456-426614174001")
    private UUID correlationId;

    @Schema(description = "Start date of the movement data export period.",
            example = "2024-01-01", required = true)
    private LocalDate startDate;

    @Schema(description = "End date of the movement data export period.",
            example = "2024-12-31", required = true)
    private LocalDate endDate;

    @Schema(description = "Status of the movement to be exported.",
            example = "ACCEPTED",
            required = true)
    private MovementStatus status;

    @Schema(description = "Email address of the user requesting the export.",
            example = "user@example.com", required = true)
    private String userEmail;
}
