package com.authenticationMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Transfer Object representing the authentication response, including the JWT token, refresh token and user ID.")
public class AuthenticationResponseDTO extends AbstractDTO {

    @Schema(description = "JWT token used for authenticating API requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Refresh token used for obtaining a new JWT token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "Tax Identification Number (NIF) of the user", example = "123456789", required = true)
    private String nif;

    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;
}