package com.portalMicroservice.dto.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "SessionRequestDTO", description = "Request object to create a payment session")
public class SessionRequestDTO {

    @NotNull
    @NotEmpty
    @Schema(description = "The currency for the payment session",
            example = "USD",
            required = true)
    private String currency;

    @Schema(description = "Optional correlation ID to track the session",
            example = "123e4567-e89b-12d3-a456-426614174000",
            nullable = true)
    private UUID correlationId;

    @Email
    @Schema(description = "Email address of the user initiating the payment session",
            example = "customer@example.com",
            nullable = true)
    private String email;

    @NotNull
    @NotEmpty
    @Schema(description = "Description of the payment session",
            example = "Order #12345",
            nullable = true)
    private String description;

    @NotNull
    @NotEmpty
    @ArraySchema(schema = @Schema(implementation = CreatePaymentItemDTO.class))
    @Schema(description = "Array of items included in the payment session")
    private CreatePaymentItemDTO[] items;
}