package com.notificationMicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Data Transfer Object representing a notification request, used for sending notifications via email.")
public class NotificationRequestDTO {

    @Schema(description = "Unique identifier (UUID) for each entity instance", example = "e12a567d-32f8-4e9a-9073-6781f9d5e423",
            nullable = true)
    private UUID id;

    @Schema(description = "Email address of the notification recipient", example = "recipient@example.com", required = true)
    private String recipient;

    @Schema(description = "Base64 encoded attachment of the report (optional)",
            example = "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64, ...",
            nullable = true)
    private String attachment;

    @Schema(description = "A URL link for resetting the password, containing a token for authentication.",
            example = "http://localhost:8080/api/v1/auth/reset-password?token=abc123xyz",
            nullable = true)
    private String resetLink;

    @Schema(description = "A URL link to the Stripe receipt, allowing users to view or download their receipt.",
            example = "https://stripe.com/receipts/example123",
            nullable = true)
    private String stripeReceiptUrl;
}