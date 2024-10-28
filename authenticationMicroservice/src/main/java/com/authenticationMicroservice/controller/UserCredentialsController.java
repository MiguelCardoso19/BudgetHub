package com.authenticationMicroservice.controller;

import com.authenticationMicroservice.dto.AuthenticationResponseDTO;
import com.authenticationMicroservice.dto.DeleteRequestDTO;
import com.authenticationMicroservice.dto.UserCredentialsDTO;
import com.authenticationMicroservice.exception.InvalidPasswordException;
import com.authenticationMicroservice.exception.UserCredentialsValidationException;
import com.authenticationMicroservice.exception.UserNotFoundException;
import com.authenticationMicroservice.service.impl.UserCredentialsServiceImpl;
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
@Tag(name = "User Credentials API", description = "APIs for managing user credentials, including registration, updating, and deletion.")
public class UserCredentialsController {
    private final UserCredentialsServiceImpl userCredentialsService;

    @Operation(summary = "Register a new user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid user data")
            })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(
            @Valid @RequestBody UserCredentialsDTO userCredentialsDTO
    ) throws UserCredentialsValidationException {
        return ResponseEntity.ok(userCredentialsService.register(userCredentialsDTO));
    }

    @Operation(summary = "Update existing user credentials",
            description = "Update the user credentials such as username, password, etc.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User credentials updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data or validation failed"),
            @ApiResponse(responseCode = "404", description = "User ID not found")
    })
    @PutMapping("/update")
    public ResponseEntity<UserCredentialsDTO> update(
            @Valid @RequestBody @Parameter(description = "Updated user credentials data")
            UserCredentialsDTO userCredentialsDTO) throws UserCredentialsValidationException, InvalidPasswordException, UserNotFoundException {
        return ResponseEntity.ok(userCredentialsService.update(userCredentialsDTO));
    }

    @Operation(summary = "Delete a user account",
            description = "Delete an existing user account by providing the user's credentials.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data or password mismatch"),
            @ApiResponse(responseCode = "404", description = "User ID not found")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(
            @Valid @RequestBody @Parameter(description = "User credentials to delete the account")
            DeleteRequestDTO deleteRequestDTO) throws InvalidPasswordException, UserNotFoundException {
        userCredentialsService.delete(deleteRequestDTO);
        return ResponseEntity.ok().build();
    }
}
