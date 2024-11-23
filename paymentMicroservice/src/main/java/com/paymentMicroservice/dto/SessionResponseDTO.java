package com.paymentMicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SessionResponseDTO", description = "Response containing session ID, URL, and correlation ID.")
public class SessionResponseDTO {

    @Schema(description = "Unique identifier for the payment session",
            example = "sess_123456789",
            required = true)
    private String sessionId;

    @Schema(description = "URL to the payment session",
            example = "https://paymentgateway.com/session/sess_123456789",
            required = true)
    private String sessionUrl;

    @Schema(description = "Correlation ID to track the session",
            example = "123e4567-e89b-12d3-a456-426614174000",
            nullable = true)
    private UUID correlationId;
}
