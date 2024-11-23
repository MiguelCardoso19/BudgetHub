package com.portalMicroservice.dto.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "Data Transfer Object for deletion requests, requiring user ID and password for verification.")
public class DeleteRequestDTO extends AbstractDTO {

    @NotNull
    @NotEmpty
    @Schema(description = "Password of the user, required to authorize deletion", example = "P@ssw0rd123", required = true)
    private String password;
}