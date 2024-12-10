package com.authenticationMicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO for resetting a user's password. Contains the token for validation and the new password.")
public class ResetPasswordRequestDTO {

    @NotEmpty
    @NotNull
    @Schema(description = "The password reset token, usually sent via email for validation.", example = "abc123xyz", required = true)
    private String token;

    @NotEmpty
    @NotNull
    @Schema(description = "The new password to set for the user.", example = "NewSecurePassword123!", required = true)
    private String newPassword;
}