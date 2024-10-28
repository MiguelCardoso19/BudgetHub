package com.authenticationMicroservice.controller;

import com.authenticationMicroservice.dto.AuthenticationResponseDTO;
import com.authenticationMicroservice.dto.SignInRequestDTO;
import com.authenticationMicroservice.exception.EmailNotFoundException;
import com.authenticationMicroservice.exception.InvalidPasswordException;
import com.authenticationMicroservice.exception.NifNotFoundException;
import com.authenticationMicroservice.exception.UserNotFoundException;
import com.authenticationMicroservice.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication API", description = "Handles user authentication, token validation, and refresh token operations.")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Sign in with user credentials",
            description = "Authenticate user and return JWT token if the credentials are valid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful, returns JWT token"),
            @ApiResponse(responseCode = "400", description = "Invalid user credentials")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponseDTO> signIn(
            @Valid @RequestBody @Parameter(description = "User credentials (username and password)") SignInRequestDTO signInRequestDTO
    ) throws InvalidPasswordException, UserNotFoundException, EmailNotFoundException {
        return ResponseEntity.ok(authenticationService.signIn(signInRequestDTO));
    }

    @Operation(summary = "Refresh the JWT token",
            description = "Refresh the user's JWT token using the refresh token.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired refresh token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDTO> refreshToken(
            @Parameter(description = "HTTP request to extract refresh token") HttpServletRequest request
    ) throws IOException, UserNotFoundException, NifNotFoundException {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }
}