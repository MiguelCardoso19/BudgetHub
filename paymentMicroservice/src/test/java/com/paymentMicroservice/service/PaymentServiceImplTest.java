package com.paymentMicroservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

import com.paymentMicroservice.dto.*;
import com.paymentMicroservice.exception.*;
import com.paymentMicroservice.model.FailedPayment;
import com.paymentMicroservice.repository.FailedPaymentRepository;
import com.paymentMicroservice.service.impl.PaymentServiceImpl;
import com.paymentMicroservice.validator.PaymentValidator;
import com.stripe.model.*;
import com.stripe.exception.StripeException;
import com.stripe.net.RequestOptions;
import com.stripe.param.CustomerSearchParams;
import com.stripe.param.PaymentIntentCancelParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.TokenCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.junit.jupiter.api.extension.ExtendWith;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@EmbeddedKafka(partitions = 1, controlledShutdown = true)
@EnableKafka
public class PaymentServiceImplTest {

    @Mock
    private PaymentValidator paymentValidator;
    @Mock
    private FailedPaymentRepository failedPaymentRepository;
    @Mock
    private RedissonClient redissonClient;
    @Mock
    private KafkaTemplate<String, CreatePaymentResponseDTO> kafkaCreatePaymentResponseTemplate;
    @Mock
    private KafkaTemplate<String, String> kafkaStringTemplate;
    @Mock
    private KafkaTemplate<String, StripeCardTokenDTO> kafkaStripeCardTokenTemplate;
    @Mock
    private KafkaTemplate<String, StripeSepaTokenDTO> kafkaStripeSepaTokenTemplate;
    @Mock
    private KafkaTemplate<String, FailedToCancelPaymentException> kafkaFailedToCancelPaymentExceptionTemplate;
    @Mock
    private KafkaTemplate<String, FailedToConfirmPaymentException> kafkaFailedToConfirmPaymentExceptionTemplate;
    @Mock
    private KafkaTemplate<String, RefundException> kafkaRefundNotPossibleExceptionTemplate;
    @Mock
    private KafkaTemplate<String, StripeCardTokenCreationException> kafkaStripeCardTokenCreationExceptionTemplate;
    @Mock
    private KafkaTemplate<String, StripeSepaTokenCreationException> kafkaStripeSepaTokenCreationExceptionTemplate;
    @Mock
    private KafkaTemplate<String, SessionResponseDTO> kafkaSessionResponseTemplate;
    @Mock
    private KafkaTemplate<String, PaymentSessionException> kafkaPaymentSessionExceptionTemplate;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @Test
    void cancelPaymentIntent_shouldSendCancelSuccessMessage_whenPaymentIntentIsValid() throws StripeException, FailedToCancelPaymentException {
        PaymentActionRequestDTO request = new PaymentActionRequestDTO();
        request.setPaymentIntentId("pi_123");

        try (MockedStatic<PaymentIntent> mockedPaymentIntent = mockStatic(PaymentIntent.class)) {
            PaymentIntent mockPaymentIntent = mock(PaymentIntent.class);
            when(mockPaymentIntent.getStatus()).thenReturn("requires_payment_method");
            mockedPaymentIntent.when(() -> PaymentIntent.retrieve("pi_123")).thenReturn(mockPaymentIntent);

            paymentService.cancelPaymentIntent(request);

            verify(mockPaymentIntent, times(1)).cancel(any(PaymentIntentCancelParams.class));
            verify(mockPaymentIntent, times(1)).getStatus();
        }
    }

    @Test
    void cancelPaymentIntent_shouldThrowException_whenPaymentIntentCannotBeCancelled() throws StripeException {
        PaymentActionRequestDTO request = new PaymentActionRequestDTO();
        request.setPaymentIntentId("pi_123");

        MockedStatic<PaymentIntent> paymentIntentMock = mockStatic(PaymentIntent.class);
        PaymentIntent mockPaymentIntent = mock(PaymentIntent.class);
        when(mockPaymentIntent.getStatus()).thenReturn("succeeded");
        paymentIntentMock.when(() -> PaymentIntent.retrieve("pi_123")).thenReturn(mockPaymentIntent);

        assertThrows(FailedToCancelPaymentException.class, () -> paymentService.cancelPaymentIntent(request));
    }

    /* Cannot send card data while in production
    @Test
    void createCardToken_shouldSendKafkaMessage_whenTokenCreationIsSuccessful() throws StripeException, StripeCardTokenCreationException {
        StripeCardTokenDTO model = new StripeCardTokenDTO();
        model.setCardNumber("4242424242424242");
        model.setExpMonth("12");
        model.setExpYear("25");
        model.setCvc("123");

        Token mockToken = mock(Token.class);
        when(mockToken.getId()).thenReturn("tok_123");

        paymentService.createCardToken(model);

        verify(kafkaStripeCardTokenTemplate, times(1))
                .send(eq("create-card-token-response-topic"), eq(model));

        assertTrue(model.isSuccess());
        assertEquals("tok_123", model.getToken());
    }
     */
}
