package com.portalMicroservice.controller.authentication;

import com.portalMicroservice.client.authentication.UserCredentialsFeignClient;
import com.portalMicroservice.dto.authentication.AuthenticationResponseDTO;
import com.portalMicroservice.dto.authentication.DeleteRequestDTO;
import com.portalMicroservice.dto.authentication.UserCredentialsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-credentials")
@RequiredArgsConstructor
@Tag(name = "User Credentials API", description = "API for managing user credentials including registration, update, and deletion.")
public class UserCredentialsController {
    private final UserCredentialsFeignClient userCredentialsFeignClient;

    @Operation(summary = "Register a new user",
            description = "Registers a new user with provided credentials and returns an authentication response containing a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully, returns authentication response"),
            @ApiResponse(responseCode = "400", description = "Invalid user credentials provided"),
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(
            @Valid @RequestBody @Parameter(description = "User credentials for registration") UserCredentialsDTO userCredentialsDTO
    ) {
        return userCredentialsFeignClient.register(userCredentialsDTO);
    }

    @Operation(summary = "Update user credentials",
            description = "Updates the user credentials and returns the updated credentials.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User credentials updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials update data provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access to update credentials"),
    })
    @PutMapping("/update")
    public ResponseEntity<UserCredentialsDTO> update(
            @Valid @RequestBody @Parameter(description = "Updated user credentials") UserCredentialsDTO userCredentialsUpdateDTO
    ) {
        return userCredentialsFeignClient.update(userCredentialsUpdateDTO);
    }

    @Operation(summary = "Delete user credentials",
            description = "Deletes the provided user credentials from the system.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User credentials deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials data for deletion"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access to delete credentials"),
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(
            @Valid @RequestBody @Parameter(description = "User credentials to delete") DeleteRequestDTO deleteRequestDTO
    ) {
        return userCredentialsFeignClient.delete(deleteRequestDTO);
    }
}