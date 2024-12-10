package com.portalMicroservice.controller.authentication;

import com.portalMicroservice.client.authentication.AuthenticationFeignClient;
import com.portalMicroservice.dto.authentication.AuthenticationResponseDTO;
import com.portalMicroservice.dto.authentication.SignInRequestDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "API for authentication, communicating with the Authentication microservice.")
public class AuthenticationController {
    private final AuthenticationFeignClient authenticationFeignClient;

    @Operation(
            summary = "Sign in using user credentials",
            description = "Authenticate a user through the proxy and return a JWT token if successful."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful, returns JWT token"),
            @ApiResponse(responseCode = "400", description = "Invalid user credentials"),
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponseDTO> signIn(
            @Valid @RequestBody @Parameter(description = "User credentials including username and password") SignInRequestDTO signInRequestDTO
    ) {
        return authenticationFeignClient.signIn(signInRequestDTO);
    }

    @Operation(
            summary = "Refresh JWT token",
            description = "Refresh the JWT token using the Authorization header through the proxy.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired refresh token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDTO> refreshToken(
            @Parameter(description = "Authorization header containing the Bearer token") HttpServletRequest request
    ) {
        return authenticationFeignClient.refreshToken(request.getHeader("Authorization"));
    }

    @Operation(
            summary = "Sign out and invalidate the user",
            description = "Invalidates the current user by communicating with the Authentication microservice.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sign-out successful, token invalidated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Token is missing or invalid")
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut(
            @Parameter(description = "Authorization header containing the Bearer token") HttpServletRequest request
    ) {
        authenticationFeignClient.signOut(request.getHeader("Authorization"));
        return ResponseEntity.noContent().build();
    }
}