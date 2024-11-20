package com.portalMicroservice.service;

import com.portalMicroservice.dto.payment.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface PaymentService {
    CreatePaymentResponseDTO createPayment(CreatePaymentDTO createPaymentDTO, String receiptEmail) throws ExecutionException, InterruptedException, TimeoutException;
    void cancelPayment(PaymentActionRequestDTO request) throws ExecutionException, InterruptedException, TimeoutException;
    void confirmPayment(PaymentActionRequestDTO request) throws ExecutionException, InterruptedException, TimeoutException;
    void refundCharge(RefundChargeRequestDTO request) throws ExecutionException, InterruptedException, TimeoutException;
    CompletableFuture<CreatePaymentResponseDTO> removePendingCreateRequestByCorrelationId(UUID correlationId);
    CompletableFuture<PaymentActionRequestDTO> removePendingActionRequestByIntentId(String id);
    CompletableFuture<RefundChargeRequestDTO> removePendingRefundRequestByIntentId(String id);
    CompletableFuture<StripeCardTokenDTO> removePendingCardTokenRequestByCorrelationId(UUID correlationId);
    CompletableFuture<StripeSepaTokenDTO> removePendingSepaTokenRequestByCorrelationId(UUID correlationId);
    StripeCardTokenDTO createCardToken(StripeCardTokenDTO model) throws ExecutionException, InterruptedException, TimeoutException;
    StripeSepaTokenDTO createSepaToken(StripeSepaTokenDTO model) throws ExecutionException, InterruptedException, TimeoutException;
}
