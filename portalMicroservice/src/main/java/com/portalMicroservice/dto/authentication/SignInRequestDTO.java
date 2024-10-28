package com.portalMicroservice.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Data Transfer Object for user sign-in requests.")
public class SignInRequestDTO {

    @Email
    @NotEmpty
    @NotNull
    @Schema(description = "Email address of the user", example = "john.keen@example.com", required = true)
    private String email;

    @NotNull
    @NotEmpty
    @Schema(description = "Password of the user, used for authentication", example = "P@ssw0rd123", required = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
