package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.payment.*;
import com.portalMicroservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final KafkaTemplate<String, CreatePaymentDTO> kafkaCreatePaymentTemplate;
    private final KafkaTemplate<String, PaymentActionRequestDTO> kafkaPaymentActionTemplate;
    private final KafkaTemplate<String, RefundChargeRequestDTO> kafkaRefundTemplate;
    private final KafkaTemplate<String, StripeCardTokenDTO> kafkaStripeCardTokenTemplate;
    private final KafkaTemplate<String, StripeSepaTokenDTO> kafkaStripeSepaTokenTemplate;
    private final KafkaTemplate<String, SessionRequestDTO> kafkaSessionRequestTemplate;

    @Value("${kafka-timeout-duration}")
    private long TIMEOUT_DURATION;

    private final ConcurrentHashMap<UUID, CompletableFuture<CreatePaymentResponseDTO>> pendingCreatePaymentIntentRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, CompletableFuture<SessionResponseDTO>> pendingCreatePaymentSessionRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CompletableFuture<PaymentActionRequestDTO>> pendingActionRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CompletableFuture<RefundChargeRequestDTO>> pendingRefundRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, CompletableFuture<StripeCardTokenDTO>> pendingCardTokenRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, CompletableFuture<StripeSepaTokenDTO>> pendingSepaTokenRequests = new ConcurrentHashMap<>();

    @Override
    public CreatePaymentResponseDTO createPaymentIntent(CreatePaymentDTO createPaymentDTO, String email) throws ExecutionException, InterruptedException, TimeoutException {
        createPaymentDTO.setCorrelationId(UUID.randomUUID());
        createPaymentDTO.setReceiptEmail(email);
        CompletableFuture<CreatePaymentResponseDTO> future = new CompletableFuture<>();
        pendingCreatePaymentIntentRequests.put(createPaymentDTO.getCorrelationId(), future);
        kafkaCreatePaymentTemplate.send("create-payment-intent-topic", email, createPaymentDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public void cancelPayment(PaymentActionRequestDTO request, String email) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<PaymentActionRequestDTO> future = new CompletableFuture<>();
        pendingActionRequests.put(request.getPaymentIntentId(), future);
        kafkaPaymentActionTemplate.send("cancel-payment-intent-topic", email, request);
        future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public void confirmPayment(PaymentActionRequestDTO request, String email) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<PaymentActionRequestDTO> future = new CompletableFuture<>();
        pendingActionRequests.put(request.getPaymentIntentId(), future);
        kafkaPaymentActionTemplate.send("confirm-payment-intent-topic", email, request);
        future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public void refundCharge(RefundChargeRequestDTO request, String email) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<RefundChargeRequestDTO> future = new CompletableFuture<>();
        pendingRefundRequests.put(request.getPaymentIntentId(), future);
        kafkaRefundTemplate.send("refund-charge-topic", email, request);
        future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public StripeCardTokenDTO createCardToken(StripeCardTokenDTO request, String email) throws ExecutionException, InterruptedException, TimeoutException {
        request.setCorrelationId(UUID.randomUUID());
        CompletableFuture<StripeCardTokenDTO> future = new CompletableFuture<>();
        pendingCardTokenRequests.put(request.getCorrelationId(), future);
        kafkaStripeCardTokenTemplate.send("create-card-token-topic", email, request);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public StripeSepaTokenDTO createSepaToken(StripeSepaTokenDTO request, String email) throws ExecutionException, InterruptedException, TimeoutException {
        request.setCorrelationId(UUID.randomUUID());
        CompletableFuture<StripeSepaTokenDTO> future = new CompletableFuture<>();
        pendingSepaTokenRequests.put(request.getCorrelationId(), future);
        kafkaStripeSepaTokenTemplate.send("create-sepa-token-topic", email, request);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public SessionResponseDTO createPaymentSession(SessionRequestDTO request, String email) throws ExecutionException, InterruptedException, TimeoutException {
        request.setCorrelationId(UUID.randomUUID());
        request.setEmail(email);
        CompletableFuture<SessionResponseDTO> future = new CompletableFuture<>();
        pendingCreatePaymentSessionRequests.put(request.getCorrelationId(), future);
        kafkaSessionRequestTemplate.send("create-payment-session-topic", email, request);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public CompletableFuture<CreatePaymentResponseDTO> removePendingCreatePaymentIntentRequestByCorrelationId(UUID correlationId) {
        return pendingCreatePaymentIntentRequests.remove(correlationId);
    }

    @Override
    public CompletableFuture<PaymentActionRequestDTO> removePendingActionRequestByIntentId(String id) {
        return pendingActionRequests.remove(id);
    }

    @Override
    public CompletableFuture<RefundChargeRequestDTO> removePendingRefundRequestByIntentId(String id) {
        return pendingRefundRequests.remove(id);
    }

    @Override
    public CompletableFuture<StripeCardTokenDTO> removePendingCardTokenRequestByCorrelationId(UUID correlationId) {
        return pendingCardTokenRequests.remove(correlationId);
    }

    @Override
    public CompletableFuture<StripeSepaTokenDTO> removePendingSepaTokenRequestByCorrelationId(UUID correlationId) {
        return pendingSepaTokenRequests.remove(correlationId);
    }

    @Override
    public CompletableFuture<SessionResponseDTO> removePendingCreatePaymentSessionRequestByCorrelationId(UUID correlationId) {
        return pendingCreatePaymentSessionRequests.remove(correlationId);
    }
}