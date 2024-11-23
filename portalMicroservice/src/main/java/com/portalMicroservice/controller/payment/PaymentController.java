package com.portalMicroservice.controller.payment;

import com.portalMicroservice.dto.payment.*;
import com.portalMicroservice.service.JwtService;
import com.portalMicroservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Tag(name = "Payment Controller", description = "Operations related to Payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final JwtService jwtService;

    @Operation(
            summary = "Create a Payment Intent",
            description = "Creates a payment intent based on the provided payment details. The payment intent is a payment request that can later be confirmed or canceled.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created payment intent"),
            @ApiResponse(responseCode = "400", description = "Invalid payment details")
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/create-payment-intent")
    public ResponseEntity<CreatePaymentResponseDTO> createPaymentIntent(
            @Valid @RequestBody CreatePaymentDTO createPaymentDTO) throws ExecutionException, InterruptedException, TimeoutException {
        return ResponseEntity.ok(paymentService.createPaymentIntent(createPaymentDTO, jwtService.getEmailFromRequest()));
    }

    @Operation(
            summary = "Cancel Payment",
            description = "Cancels a pending payment based on the provided payment action request.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully canceled payment"),
            @ApiResponse(responseCode = "400", description = "Invalid payment details")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelPayment(@Valid @RequestBody PaymentActionRequestDTO request) throws ExecutionException, InterruptedException, TimeoutException {
        paymentService.cancelPayment(request, jwtService.getEmailFromRequest());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Confirm Payment",
            description = "Confirms a pending payment based on the provided payment action request.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully confirmed payment"),
            @ApiResponse(responseCode = "400", description = "Invalid payment details")
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(@Valid @RequestBody PaymentActionRequestDTO request) throws ExecutionException, InterruptedException, TimeoutException {
        paymentService.confirmPayment(request, jwtService.getEmailFromRequest());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Refund Charge",
            description = "Initiates a refund for a charge based on the provided refund request.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully refunded charge"),
            @ApiResponse(responseCode = "400", description = "Invalid refund details")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/refund")
    public ResponseEntity<String> refundCharge(@Valid @RequestBody RefundChargeRequestDTO request) throws ExecutionException, InterruptedException, TimeoutException {
        paymentService.refundCharge(request, jwtService.getEmailFromRequest());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Create Card Token",
            description = "Creates a Stripe card token from the provided card details.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created card token"),
            @ApiResponse(responseCode = "400", description = "Invalid card details")
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/create-card-token")
    public ResponseEntity<StripeCardTokenDTO> createCardToken(@Valid @RequestBody StripeCardTokenDTO request) throws ExecutionException, InterruptedException, TimeoutException {
        return ResponseEntity.ok(paymentService.createCardToken(request, jwtService.getEmailFromRequest()));
    }

    @Operation(
            summary = "Create SEPA Token",
            description = "Creates a Stripe SEPA token from the provided bank account details.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created SEPA token"),
            @ApiResponse(responseCode = "400", description = "Invalid bank details")
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/create-sepa-token")
    public ResponseEntity<StripeSepaTokenDTO> createSepaToken(@Valid @RequestBody StripeSepaTokenDTO request) throws ExecutionException, InterruptedException, TimeoutException {
        return ResponseEntity.ok(paymentService.createSepaToken(request, jwtService.getEmailFromRequest()));
    }

    @Operation(
            summary = "Create Payment Session",
            description = "Creates a payment session to handle the payment process.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created payment session"),
            @ApiResponse(responseCode = "400", description = "Invalid session details")
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/create-payment-session")
    public ResponseEntity<SessionResponseDTO> createPaymentSession(@Valid @RequestBody SessionRequestDTO request) throws ExecutionException, InterruptedException, TimeoutException {
        return ResponseEntity.ok(paymentService.createPaymentSession(request, jwtService.getEmailFromRequest()));
    }
}