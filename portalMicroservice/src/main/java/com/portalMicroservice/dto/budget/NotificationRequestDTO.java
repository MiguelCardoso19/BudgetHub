package com.portalMicroservice.dto.budget;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Data Transfer Object representing a notification request, used for sending notifications via email.")
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {

    @Schema(description = "Email address of the notification recipient",
            example = "recipient@example.com", required = true)
    private String recipient;

    @Schema(description = "Base64 encoded attachment of the report (optional)",
            example = "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64, ...",
            nullable = true)
    private String attachment;
}
