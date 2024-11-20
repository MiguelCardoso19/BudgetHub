package com.paymentMicroservice.service.impl;

import com.paymentMicroservice.dto.*;
import com.paymentMicroservice.exception.BudgetExceededException;
import com.paymentMicroservice.exception.FailedToCancelPaymentException;
import com.paymentMicroservice.exception.FailedToConfirmPaymentException;
import com.paymentMicroservice.model.FailedPayment;
import com.paymentMicroservice.repository.FailedPaymentRepository;
import com.paymentMicroservice.service.PaymentService;
import com.paymentMicroservice.util.PaymentUtils;
import com.paymentMicroservice.validator.PaymentValidator;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.Token;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCancelParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.HOURS;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentValidator paymentValidator;
    private final FailedPaymentRepository failedPaymentRepository;
    private final RedissonClient redissonClient;
    private final KafkaTemplate<String, CreatePaymentResponseDTO> kafkaCreatePaymentResponseTemplate;
    private final KafkaTemplate<String, String> kafkaStringTemplate;
    private final KafkaTemplate<String, StripeCardTokenDTO> kafkaStripeCardTokenTemplate;
    private final KafkaTemplate<String, StripeSepaTokenDTO> kafkaStripeSepaTokenTemplate;
    private final KafkaTemplate<String, FailedToCancelPaymentException> kafkaFailedToCancelPaymentExceptionTemplate;
    private final KafkaTemplate<String, FailedToConfirmPaymentException> kafkaFailedToConfirmPaymentExceptionTemplate;

    @Value("${STRIPE_PRIVATE_KEY}")
    private String STRIPE_PRIVATE_KEY;

    @Value("${MAX_RETRY_ATTEMPTS}")
    private int MAX_RETRY_ATTEMPTS;

    @Value("${PAYMENT_RETRY_LOCK_KEY}")
    private String PAYMENT_RETRY_LOCK_KEY;

    @PostConstruct
    private void initStripe() {
        Stripe.apiKey = STRIPE_PRIVATE_KEY;
    }

    @Override
    @KafkaListener(topics = "create-payment-intent-topic", groupId = "create_payment_group", concurrency = "10", containerFactory = "createPaymentRequestKafkaListenerContainerFactory")
    public void createPaymentIntent(CreatePaymentDTO createPaymentDTO) throws StripeException, BudgetExceededException, ExecutionException, InterruptedException, TimeoutException {
        paymentValidator.validateFundsForPayment(createPaymentDTO);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(PaymentUtils.calculateAmount(createPaymentDTO) * 100L)
                .setReceiptEmail(createPaymentDTO.getReceiptEmail())
                .putAllMetadata(PaymentUtils.buildMetadata(createPaymentDTO))
                .setCurrency(createPaymentDTO.getCurrency())
                .setPaymentMethod(createPaymentDTO.getPaymentMethodId())
                .build();

        RequestOptions requestOptions = RequestOptions.builder()
                .setIdempotencyKey(createPaymentDTO.getCorrelationId().toString())
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params, requestOptions);
        kafkaCreatePaymentResponseTemplate.send("create-payment-intent-response-topic", new CreatePaymentResponseDTO(paymentIntent.getId(), createPaymentDTO.getCorrelationId()));
    }

    @Override
    @KafkaListener(topics = "cancel-payment-intent-topic", groupId = "payment_action_request_group", concurrency = "10", containerFactory = "paymentActionRequestKafkaListenerContainerFactory")
    public void cancelPaymentIntent(PaymentActionRequestDTO request) throws StripeException, FailedToCancelPaymentException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(request.getPaymentIntentId());

        if ("requires_payment_method".equals(paymentIntent.getStatus()) ||
                "requires_confirmation".equals(paymentIntent.getStatus())) {
            paymentIntent.cancel(PaymentIntentCancelParams.builder().build());
            kafkaStringTemplate.send("payment-action-success-response-topic", request.getPaymentIntentId());
        } else {
            kafkaFailedToCancelPaymentExceptionTemplate.send("failed-to-cancel-payment-response", new FailedToCancelPaymentException(request.getPaymentIntentId()));
            throw new FailedToCancelPaymentException(request.getPaymentIntentId());
        }
    }

    @Override
    @KafkaListener(topics = "confirm-payment-intent-topic", groupId = "payment_action_request_group", concurrency = "10", containerFactory = "paymentActionRequestKafkaListenerContainerFactory")
    public void confirmPaymentIntent(PaymentActionRequestDTO request) throws FailedToConfirmPaymentException, StripeException {
        PaymentIntentConfirmParams confirmParams = PaymentIntentConfirmParams.builder()
                .setReturnUrl("http://localhost:8084/complete.html")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.retrieve(request.getPaymentIntentId());

        if ("canceled".equals(paymentIntent.getStatus())) {
            kafkaFailedToConfirmPaymentExceptionTemplate.send("failed-to-confirm-payment-response", new FailedToConfirmPaymentException(paymentIntent.getId()));
            throw new FailedToConfirmPaymentException(paymentIntent.getId());
        }

        try {
            paymentIntent.confirm(confirmParams);
            kafkaStringTemplate.send("payment-action-success-response-topic", request.getPaymentIntentId());
        } catch (StripeException e) {
            PaymentUtils.saveFailedPayment(request.getPaymentIntentId(), failedPaymentRepository);
            kafkaFailedToConfirmPaymentExceptionTemplate.send("failed-to-confirm-payment-response", new FailedToConfirmPaymentException(paymentIntent.getId()));
            throw new FailedToConfirmPaymentException(paymentIntent.getId());
        }
    }

    @Override
    @KafkaListener(topics = "refund-charge-topic", groupId = "refund_charge_request_group", concurrency = "10", containerFactory = "refundChargeRequestKafkaListenerContainerFactory")
    public void refundCharge(RefundChargeRequestDTO request) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(request.getPaymentIntentId());
        Charge charge = Charge.retrieve(paymentIntent.getLatestCharge());
        Refund.create(Map.of("charge", charge.getId()));
        kafkaStringTemplate.send("refund-charge-success-response-topic", request.getPaymentIntentId());
    }

    @Override
    @KafkaListener(topics = "create-card-token-topic", groupId = "create_card_token_group", concurrency = "10", containerFactory = "stripeCardTokenKafkaListenerContainerFactory")
    public void createCardToken(StripeCardTokenDTO model) throws StripeException {
        Map<String, Object> card = new HashMap<>();
        card.put("number", model.getCardNumber());
        card.put("exp_month", Integer.parseInt(model.getExpMonth()));
        card.put("exp_year", Integer.parseInt(model.getExpYear()));
        card.put("cvc", model.getCvc());
        Map<String, Object> params = new HashMap<>();
        params.put("card", card);
        Token token = Token.create(params);
        if (token != null && token.getId() != null) {
            model.setSuccess(true);
            model.setToken(token.getId());
        }
        kafkaStripeCardTokenTemplate.send("create-card-token-response-topic", model);
    }

    @Override
    @KafkaListener(topics = "create-sepa-token-topic", groupId = "create_sepa_token_group", concurrency = "10", containerFactory = "stripeSepaTokenKafkaListenerContainerFactory")
    public void createSepaToken(StripeSepaTokenDTO model) throws StripeException {
        Map<String, Object> sepaDebitParams = new HashMap<>();
        sepaDebitParams.put("iban", model.getIban());
        sepaDebitParams.put("account_holder_name", model.getAccountHolderName());
        sepaDebitParams.put("account_holder_type", model.getAccountHolderType());
        Map<String, Object> params = new HashMap<>();
        params.put("sepa_debit", sepaDebitParams);
        Token token = Token.create(params);
        if (token != null && token.getId() != null) {
            model.setSuccess(true);
            model.setToken(token.getId());
        }
        kafkaStripeSepaTokenTemplate.send("create-sepa-token-response-topic", model);
    }

    @Scheduled(cron = "${RETRY_DELAY}")
    @Transactional
    @Override
    public void retryFailedPayments() throws StripeException {
        RLock lock = redissonClient.getLock(PAYMENT_RETRY_LOCK_KEY);
        try {
            if (lock.tryLock(0, 1, HOURS)) {
                try {
                    for (FailedPayment failedPayment : failedPaymentRepository.findByRetryableTrue()) {
                        if (failedPayment.getRetryAttempts() < MAX_RETRY_ATTEMPTS) {
                            if (retryToConfirmPaymentIntent(failedPayment.getClientSecret())) {
                                failedPayment.setRetryable(false);
                            } else {
                                failedPayment.setRetryAttempts(failedPayment.getRetryAttempts() + 1);
                            }
                            failedPayment.setLastAttemptTime(LocalDateTime.now());
                            failedPaymentRepository.save(failedPayment);
                        } else {
                            failedPayment.setRetryable(false);
                            failedPaymentRepository.save(failedPayment);
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean retryToConfirmPaymentIntent(String clientSecret) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(clientSecret);

        if (paymentIntent.getStatus().equals("requires_payment_method") ||
                paymentIntent.getStatus().equals("requires_confirmation")) {
            try {
                paymentIntent.confirm(PaymentIntentConfirmParams.builder()
                        .setReturnUrl("http://localhost:8084/complete.html")
                        .build());
                return true;
            } catch (StripeException e) {
                return false;
            }
        }
        return false;
    }
}