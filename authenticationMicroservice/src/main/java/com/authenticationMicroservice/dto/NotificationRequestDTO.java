package com.authenticationMicroservice.dto;

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

    @Schema(description = "A URL link for resetting the password, containing a token for authentication.",
            example = "http://localhost:8080/api/v1/auth/reset-password?token=abc123xyz",
            nullable = true)
    private String resetLink;

}
