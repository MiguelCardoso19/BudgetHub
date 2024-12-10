package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.payment.*;
import com.portalMicroservice.exception.budget.BudgetExceededException;
import com.portalMicroservice.exception.payment.*;
import com.portalMicroservice.service.PaymentEventListenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PaymentEventListenerServiceImpl implements PaymentEventListenerService {
    private final PaymentServiceImpl paymentService;

    @Override
    @KafkaListener(topics = "create-payment-intent-response-topic", groupId = "create_payment_group", concurrency = "10", containerFactory = "createPaymentResponseKafkaListenerContainerFactory")
    public void handleCreatePaymentResponse(CreatePaymentResponseDTO response) {
        CompletableFuture<CreatePaymentResponseDTO> future = paymentService.removePendingCreatePaymentIntentRequestByCorrelationId(response.getCorrelationId());
        if (future != null) {
            future.complete(response);
        }
    }

    @Override
    @KafkaListener(topics = "payment-action-success-response-topic", groupId = "string_group", concurrency = "10")
    public void handleSuccessPaymentActionResponse(String id) {
        CompletableFuture<PaymentActionRequestDTO> future = paymentService.removePendingActionRequestByIntentId(id);
        if (future != null) {
            future.complete(null);
        }
    }

    @Override
    @KafkaListener(topics = "refund-charge-success-response-topic", groupId = "string_group", concurrency = "10")
    public void handleSuccessRefundResponse(String id) {
        CompletableFuture<RefundChargeRequestDTO> future = paymentService.removePendingRefundRequestByIntentId(id);
        if (future != null) {
            future.complete(null);
        }
    }

    @Override
    @KafkaListener(topics = "create-card-token-response-topic", groupId = "stripe_card_token_group", concurrency = "10", containerFactory = "stripeCardTokenKafkaListenerContainerFactory")
    public void handleCardTokenResponse(StripeCardTokenDTO response) {
        CompletableFuture<StripeCardTokenDTO> future = paymentService.removePendingCardTokenRequestByCorrelationId(response.getCorrelationId());
        if (future != null) {
            future.complete(response);
        }
    }

    @Override
    @KafkaListener(topics = "create-sepa-token-response-topic", groupId = "stripe_sepa_token_group", concurrency = "10", containerFactory = "stripeSepaTokenKafkaListenerContainerFactory")
    public void handleSepaTokenResponse(StripeSepaTokenDTO response) {
        CompletableFuture<StripeSepaTokenDTO> future = paymentService.removePendingSepaTokenRequestByCorrelationId(response.getCorrelationId());
        if (future != null) {
            future.complete(response);
        }
    }

    @Override
    @KafkaListener(topics = "failed-to-cancel-payment-response", groupId = "failed_to_cancel_payment_response_group", concurrency = "10", containerFactory = "failedToCancelPaymentExceptionKafkaListenerContainerFactory")
    public void handleFailedToCancelPaymentExceptionResponse(FailedToCancelPaymentException errorPayload) {
        CompletableFuture<PaymentActionRequestDTO> future = paymentService.removePendingActionRequestByIntentId(errorPayload.getCorrelationId());
        if (future != null) {
            future.completeExceptionally(new FailedToCancelPaymentException(errorPayload.getCorrelationId()));
        }
    }

    @Override
    @KafkaListener(topics = "failed-to-confirm-payment-response", groupId = "failed_to_confirm_payment_response_group", concurrency = "10", containerFactory = "failedToConfirmPaymentExceptionKafkaListenerContainerFactory")
    public void handleFailedToConfirmPaymentExceptionResponse(FailedToConfirmPaymentException errorPayload) {
        CompletableFuture<PaymentActionRequestDTO> future = paymentService.removePendingActionRequestByIntentId(errorPayload.getCorrelationId());
        if (future != null) {
            future.completeExceptionally(new FailedToConfirmPaymentException(errorPayload.getCorrelationId()));
        }
    }

    @Override
    @KafkaListener(topics = "payment-exceeded-available-funds-response", groupId = "payment_exceeded_available_funds_response_group", concurrency = "10", containerFactory = "budgetExceededForPaymentExceptionKafkaListenerContainerFactory")
    public void handleBudgetExceededForPaymentExceptionResponse(BudgetExceededException errorPayload) {
        CompletableFuture<CreatePaymentResponseDTO> future = paymentService.removePendingCreatePaymentIntentRequestByCorrelationId(errorPayload.getId());
        if (future != null) {
            future.completeExceptionally(new BudgetExceededException(errorPayload.getMessage()));
        }
    }

    @Override
    @KafkaListener(topics = "refund-exception-response", groupId = "refund_exception_response_group", concurrency = "10", containerFactory = "refundExceptionKafkaListenerContainerFactory")
    public void handleRefundExceptionResponse(RefundException errorPayload) {
        CompletableFuture<RefundChargeRequestDTO> future = paymentService.removePendingRefundRequestByIntentId(errorPayload.getCorrelationId());
        if (future != null) {
            future.completeExceptionally(new RefundException());
        }
    }

    @Override
    @KafkaListener(topics = "stripe-card-token-creation-exception-response", groupId = "stripe_card_token_creation_exception_response_group", concurrency = "10", containerFactory = "stripeCardTokenCreationExceptionKafkaListenerContainerFactory")
    public void handleStripeCardTokenCreationException(StripeCardTokenCreationException errorPayload) {
        CompletableFuture<StripeCardTokenDTO> future = paymentService.removePendingCardTokenRequestByCorrelationId(errorPayload.getCorrelationId());
        if (future != null) {
            future.completeExceptionally(new StripeCardTokenCreationException());
        }
    }

    @Override
    @KafkaListener(topics = "stripe-sepa-token-creation-exception-response", groupId = "stripe_sepa_token_creation_exception_response_group", concurrency = "10", containerFactory = "stripeSepaTokenCreationExceptionKafkaListenerContainerFactory")
    public void handleStripeSepaTokenCreationException(StripeSepaTokenCreationException errorPayload) {
        CompletableFuture<StripeSepaTokenDTO> future = paymentService.removePendingSepaTokenRequestByCorrelationId(errorPayload.getCorrelationId());
        if (future != null) {
            future.completeExceptionally(new StripeSepaTokenCreationException());
        }
    }

    @Override
    @KafkaListener(topics = "create-payment-session-response-topic", groupId = "payment_session_response_group", concurrency = "10", containerFactory = "sessionResponseKafkaListenerContainerFactory")
    public void handlePaymentSessionResponse(SessionResponseDTO response) {
        CompletableFuture<SessionResponseDTO> future = paymentService.removePendingCreatePaymentSessionRequestByCorrelationId(response.getCorrelationId());
        if (future != null) {
            future.complete(response);
        }
    }

    @Override
    @KafkaListener(topics = "payment-session-exception-response-topic", groupId = "payment_session_exception_response_group", concurrency = "10", containerFactory = "paymentSessionExceptionKafkaListenerContainerFactory")
    public void handlePaymentSessionException(PaymentSessionException errorPayload) {
        CompletableFuture<SessionResponseDTO> future = paymentService.removePendingCreatePaymentSessionRequestByCorrelationId(errorPayload.getCorrelationId());
        if (future != null) {
            future.completeExceptionally(new PaymentSessionException(errorPayload.getMessage().substring(errorPayload.getMessage().indexOf(":") + 1).trim()));
        }
    }
}