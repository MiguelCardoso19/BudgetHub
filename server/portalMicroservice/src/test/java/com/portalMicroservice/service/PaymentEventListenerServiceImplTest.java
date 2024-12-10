package com.portalMicroservice.service;

import com.portalMicroservice.dto.payment.*;
import com.portalMicroservice.exception.budget.BudgetExceededException;
import com.portalMicroservice.exception.payment.*;
import com.portalMicroservice.service.impl.PaymentEventListenerServiceImpl;
import com.portalMicroservice.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@EnableKafka
class PaymentEventListenerServiceImplTest {

    @Mock
    private PaymentServiceImpl paymentService;

    @InjectMocks
    private PaymentEventListenerServiceImpl paymentEventListenerService;

    private UUID id;

    private UUID correlationId;

    @BeforeEach
    void setUp() {
        openMocks(this);
        id = UUID.randomUUID();
        correlationId = UUID.randomUUID();
    }

    @Test
    void testHandleCreatePaymentResponse() {
        CreatePaymentResponseDTO response = new CreatePaymentResponseDTO(id.toString(), correlationId);

        CompletableFuture<CreatePaymentResponseDTO> future = new CompletableFuture<>();
        when(paymentService.removePendingCreatePaymentIntentRequestByCorrelationId(response.getCorrelationId())).thenReturn(future);

        paymentEventListenerService.handleCreatePaymentResponse(response);

        verify(paymentService).removePendingCreatePaymentIntentRequestByCorrelationId(response.getCorrelationId());
        future.complete(response);
    }

    @Test
    void testHandleSuccessPaymentActionResponse() {
        CompletableFuture<PaymentActionRequestDTO> future = new CompletableFuture<>();
        when(paymentService.removePendingActionRequestByIntentId(id.toString())).thenReturn(future);

        paymentEventListenerService.handleSuccessPaymentActionResponse(id.toString());

        verify(paymentService).removePendingActionRequestByIntentId(id.toString());
        future.complete(null);
    }

    @Test
    void testHandleSuccessRefundResponse() {
        CompletableFuture<RefundChargeRequestDTO> future = new CompletableFuture<>();
        when(paymentService.removePendingRefundRequestByIntentId(id.toString())).thenReturn(future);

        paymentEventListenerService.handleSuccessRefundResponse(id.toString());

        verify(paymentService).removePendingRefundRequestByIntentId(id.toString());
        future.complete(null);
    }

    @Test
    void testHandleCardTokenResponse() {
        StripeCardTokenDTO response = new StripeCardTokenDTO();
        response.setCorrelationId(correlationId);

        CompletableFuture<StripeCardTokenDTO> future = new CompletableFuture<>();
        when(paymentService.removePendingCardTokenRequestByCorrelationId(response.getCorrelationId())).thenReturn(future);

        paymentEventListenerService.handleCardTokenResponse(response);

        verify(paymentService).removePendingCardTokenRequestByCorrelationId(response.getCorrelationId());
        future.complete(response);
    }

    @Test
    void testHandleSepaTokenResponse() {
        StripeSepaTokenDTO response = new StripeSepaTokenDTO();
        response.setCorrelationId(correlationId);

        CompletableFuture<StripeSepaTokenDTO> future = new CompletableFuture<>();
        when(paymentService.removePendingSepaTokenRequestByCorrelationId(response.getCorrelationId())).thenReturn(future);

        paymentEventListenerService.handleSepaTokenResponse(response);

        verify(paymentService).removePendingSepaTokenRequestByCorrelationId(response.getCorrelationId());
        future.complete(response);
    }

    @Test
    void testHandleFailedToCancelPaymentExceptionResponse() {
        FailedToCancelPaymentException errorPayload = new FailedToCancelPaymentException(correlationId.toString());

        CompletableFuture<PaymentActionRequestDTO> future = new CompletableFuture<>();
        when(paymentService.removePendingActionRequestByIntentId(errorPayload.getCorrelationId())).thenReturn(future);

        paymentEventListenerService.handleFailedToCancelPaymentExceptionResponse(errorPayload);

        verify(paymentService).removePendingActionRequestByIntentId(errorPayload.getCorrelationId());
        future.completeExceptionally(new FailedToCancelPaymentException(errorPayload.getCorrelationId()));
    }

    @Test
    void testHandleBudgetExceededForPaymentExceptionResponse() {
        BudgetExceededException errorPayload = new BudgetExceededException(id, "fakedata");

        CompletableFuture<CreatePaymentResponseDTO> future = new CompletableFuture<>();
        when(paymentService.removePendingCreatePaymentIntentRequestByCorrelationId(errorPayload.getId())).thenReturn(future);

        paymentEventListenerService.handleBudgetExceededForPaymentExceptionResponse(errorPayload);

        verify(paymentService).removePendingCreatePaymentIntentRequestByCorrelationId(errorPayload.getId());
        future.completeExceptionally(new BudgetExceededException(errorPayload.getMessage()));
    }

    @Test
    void testHandleRefundExceptionResponse() {
        RefundException errorPayload = new RefundException(correlationId.toString());

        CompletableFuture<RefundChargeRequestDTO> future = new CompletableFuture<>();
        when(paymentService.removePendingRefundRequestByIntentId(errorPayload.getCorrelationId())).thenReturn(future);

        paymentEventListenerService.handleRefundExceptionResponse(errorPayload);

        verify(paymentService).removePendingRefundRequestByIntentId(errorPayload.getCorrelationId());
        future.completeExceptionally(new RefundException());
    }

    @Test
    void testHandleStripeCardTokenCreationExceptionResponse() {
        StripeCardTokenCreationException errorPayload = new StripeCardTokenCreationException(correlationId);

        CompletableFuture<StripeCardTokenDTO> future = new CompletableFuture<>();
        when(paymentService.removePendingCardTokenRequestByCorrelationId(errorPayload.getCorrelationId())).thenReturn(future);

        paymentEventListenerService.handleStripeCardTokenCreationException(errorPayload);

        verify(paymentService).removePendingCardTokenRequestByCorrelationId(errorPayload.getCorrelationId());
        future.completeExceptionally(new StripeCardTokenCreationException());
    }

    @Test
    void testHandleStripeSepaTokenCreationExceptionResponse() {
        StripeSepaTokenCreationException errorPayload = new StripeSepaTokenCreationException(correlationId);

        CompletableFuture<StripeSepaTokenDTO> future = new CompletableFuture<>();
        when(paymentService.removePendingSepaTokenRequestByCorrelationId(errorPayload.getCorrelationId())).thenReturn(future);

        paymentEventListenerService.handleStripeSepaTokenCreationException(errorPayload);

        verify(paymentService).removePendingSepaTokenRequestByCorrelationId(errorPayload.getCorrelationId());
        future.completeExceptionally(new StripeSepaTokenCreationException());
    }

    @Test
    void testHandlePaymentSessionResponse() {
        SessionResponseDTO response = new SessionResponseDTO();
        response.setCorrelationId(correlationId);

        CompletableFuture<SessionResponseDTO> future = new CompletableFuture<>();
        when(paymentService.removePendingCreatePaymentSessionRequestByCorrelationId(response.getCorrelationId())).thenReturn(future);

        paymentEventListenerService.handlePaymentSessionResponse(response);

        verify(paymentService).removePendingCreatePaymentSessionRequestByCorrelationId(response.getCorrelationId());
        future.complete(response);
    }

    @Test
    void testHandlePaymentSessionExceptionResponse() {
        PaymentSessionException errorPayload = new PaymentSessionException("fakedata", correlationId);

        CompletableFuture<SessionResponseDTO> future = new CompletableFuture<>();
        when(paymentService.removePendingCreatePaymentSessionRequestByCorrelationId(errorPayload.getCorrelationId())).thenReturn(future);

        paymentEventListenerService.handlePaymentSessionException(errorPayload);

        verify(paymentService).removePendingCreatePaymentSessionRequestByCorrelationId(errorPayload.getCorrelationId());
        future.completeExceptionally(new PaymentSessionException(errorPayload.getMessage()));
    }
}