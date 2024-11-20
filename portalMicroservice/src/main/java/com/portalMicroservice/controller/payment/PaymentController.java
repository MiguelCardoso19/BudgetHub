package com.portalMicroservice.controller.payment;

import com.portalMicroservice.dto.payment.*;
import com.portalMicroservice.service.JwtService;
import com.portalMicroservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<CreatePaymentResponseDTO> createPayment(@Valid @RequestBody CreatePaymentDTO createPaymentDTO) throws ExecutionException, InterruptedException, TimeoutException {
        return ResponseEntity.ok(paymentService.createPayment(createPaymentDTO, jwtService.getEmailFromRequest()));
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelPayment(@Valid @RequestBody PaymentActionRequestDTO request) throws ExecutionException, InterruptedException, TimeoutException {
        paymentService.cancelPayment(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(@Valid @RequestBody PaymentActionRequestDTO request) throws ExecutionException, InterruptedException, TimeoutException {
        paymentService.confirmPayment(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refund")
    public ResponseEntity<String> refundCharge(@Valid @RequestBody RefundChargeRequestDTO request) throws ExecutionException, InterruptedException, TimeoutException {
        paymentService.refundCharge(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create-card-token")
    public ResponseEntity<StripeCardTokenDTO> createCardToken(@RequestBody StripeCardTokenDTO request) throws ExecutionException, InterruptedException, TimeoutException {
        return ResponseEntity.ok(paymentService.createCardToken(request));
    }

    @PostMapping("/create-sepa-token")
    public ResponseEntity<StripeSepaTokenDTO> createSepaToken(@RequestBody StripeSepaTokenDTO request) throws ExecutionException, InterruptedException, TimeoutException {
        return ResponseEntity.ok(paymentService.createSepaToken(request));
    }
}