package com.paymentMicroservice.service.impl;

import com.google.gson.Gson;
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
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
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

    private final Gson gson = new Gson();

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

    // String to make JS frontend happy
    @Override
    public String createPaymentIntent(CreatePaymentDTO createPaymentDTO) throws StripeException, BudgetExceededException, ExecutionException, InterruptedException, TimeoutException {
        paymentValidator.validateFundsForPayment(createPaymentDTO);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(PaymentUtils.calculateAmount(createPaymentDTO) * 100L)
                .setReceiptEmail(createPaymentDTO.getReceiptEmail())
//                .addPaymentMethodType("card")
//                .addPaymentMethodType("sepa_debit")
//                .addPaymentMethodType("multibanco")
//                .addPaymentMethodType("paypal")
//                .addPaymentMethodType("revolut_pay")
//                .setCurrency("EUR")
                .putAllMetadata(PaymentUtils.buildMetadata(createPaymentDTO))
                .setCurrency(createPaymentDTO.getCurrency())
                .setPaymentMethod(createPaymentDTO.getPaymentMethodId())
                .build();

        RequestOptions requestOptions = RequestOptions.builder()
                .setIdempotencyKey(UUID.randomUUID().toString())
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params, requestOptions);

        CreatePaymentResponseDTO paymentResponse = new CreatePaymentResponseDTO(paymentIntent.getClientSecret(), paymentIntent.getId());
        return gson.toJson(paymentResponse);
    }

    @Override
    public void cancelPaymentIntent(PaymentConfirmationRequestDTO request) throws StripeException, FailedToCancelPaymentException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(request.getClientSecret());

        if ("requires_payment_method".equals(paymentIntent.getStatus()) ||
                "requires_confirmation".equals(paymentIntent.getStatus())) {
            paymentIntent.cancel(PaymentIntentCancelParams.builder().build());
        } else {
            throw new FailedToCancelPaymentException(request.getClientSecret().substring(0, request.getClientSecret().indexOf("_")));
        }
    }

    @Override
    public void confirmPaymentIntent(PaymentConfirmationRequestDTO request) throws FailedToConfirmPaymentException, StripeException {
        PaymentIntentConfirmParams confirmParams = PaymentIntentConfirmParams.builder()
                .setReturnUrl("http://localhost:8084/complete.html")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.retrieve(request.getClientSecret());

        if ("canceled".equals(paymentIntent.getStatus())) {
            throw new FailedToConfirmPaymentException(paymentIntent.getId());
        }

        try {
            paymentIntent.confirm(confirmParams);
        } catch (StripeException e) {
            saveFailedPayment(request.getClientSecret());
            throw new FailedToConfirmPaymentException(paymentIntent.getId());
        }
    }

    @Override
    public void refundCharge(RefundChargeRequestDTO request) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(request.getPaymentIntentId());
        Charge charge = Charge.retrieve(paymentIntent.getLatestCharge());
        Refund.create(Map.of("charge", charge.getId()));
    }

    @Override
    public StripeCardTokenDTO createCardToken(StripeCardTokenDTO model) throws StripeException {
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
        return model;
    }

    @Override
    public StripeSepaTokenDTO createSepaToken(StripeSepaTokenDTO model) throws StripeException {
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
        return model;
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

    private void saveFailedPayment(String clientSecret) {
        FailedPayment failedPayment = new FailedPayment();
        failedPayment.setClientSecret(clientSecret);
        failedPayment.setRetryAttempts(0);
        failedPayment.setLastAttemptTime(LocalDateTime.now());
        failedPayment.setRetryable(true);
        failedPaymentRepository.save(failedPayment);
    }
}