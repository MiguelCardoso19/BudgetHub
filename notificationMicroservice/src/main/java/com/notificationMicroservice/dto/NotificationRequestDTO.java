package com.notificationMicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Data Transfer Object representing a notification request, used for sending notifications via email.")
public class NotificationRequestDTO {

    @Schema(description = "Unique identifier (UUID) for each entity instance", example = "e12a567d-32f8-4e9a-9073-6781f9d5e423")
    private UUID id;

    @Schema(description = "Email address of the notification recipient", example = "recipient@example.com", required = true)
    private String recipient;

    @Schema(description = "Base64 encoded attachment of the report (optional)", example = "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64, ...")
    private String attachment;

    private String resetLink;

}