package com.portalMicroservice.controller.authentication;

import com.portalMicroservice.client.authentication.UserCredentialsFeignClient;
import com.portalMicroservice.dto.authentication.AuthenticationResponseDTO;
import com.portalMicroservice.dto.authentication.DeleteRequestDTO;
import com.portalMicroservice.dto.authentication.ResetPasswordRequestDTO;
import com.portalMicroservice.dto.authentication.UserCredentialsDTO;
import com.portalMicroservice.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-credentials")
@RequiredArgsConstructor
@Tag(name = "User Credentials Controller", description = "API for managing user credentials including registration, update, and deletion.")
public class UserCredentialsController {
    private final UserCredentialsFeignClient userCredentialsFeignClient;
    private final JwtService jwtService;

    @Operation(summary = "Register a new user",
            description = "Registers a new user with provided credentials and returns an authentication response containing a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully, returns authentication response"),
            @ApiResponse(responseCode = "400", description = "Invalid user credentials provided"),
    })
    @PreAuthorize("permitAll()")
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
    @PreAuthorize("permitAll()")
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
    @PreAuthorize("permitAll()")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(
            @Valid @RequestBody @Parameter(description = "User credentials to delete") DeleteRequestDTO deleteRequestDTO
    ) {
        return userCredentialsFeignClient.delete(deleteRequestDTO);
    }

    @Operation(
            summary = "Recover password by sending a reset link to the logged user's email",
            description = "Sends a password recovery email to the user with a reset token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password recovery email sent successfully"),
                    @ApiResponse(responseCode = "404", description = "Email not found in the system")
            })
    @PreAuthorize("permitAll()")
    @PostMapping("/recover-password")
    public ResponseEntity<Void> recoverPassword() {
        userCredentialsFeignClient.recoverPassword(jwtService.getEmailFromRequest());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Reset the user's password using the provided reset token",
            description = "This method resets the user's password after validating the reset token and setting the new password.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password reset successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid password format or request data"),
                    @ApiResponse(responseCode = "404", description = "Invalid or expired token")
            })
    @PreAuthorize("permitAll()")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody
            @Parameter(description = "Reset password request containing the reset token and the new password", required = true)
            ResetPasswordRequestDTO resetPasswordRequestDTO
    ) {
        userCredentialsFeignClient.resetPassword(resetPasswordRequestDTO);
        return ResponseEntity.noContent().build();
    }
}