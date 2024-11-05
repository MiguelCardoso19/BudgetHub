package com.budgetMicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object representing a notification request, used for sending notifications via email.")
public class NotificationRequestDTO {

    @Schema(description = "Email address of the notification recipient", example = "recipient@example.com", required = true)
    private String recipient;

    @Schema(description = "Subject of the notification email", example = "Your Movements Report", required = true)
    private String subject;

    @Schema(description = "Body content of the notification email", example = "Please find attached your movements report.", required = true)
    private String body;

    @Schema(description = "Base64 encoded attachment of the report (optional)", example = "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64, ...")
    private String attachment;
}