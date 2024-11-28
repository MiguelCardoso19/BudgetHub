package com.paymentMicroservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

import com.paymentMicroservice.dto.*;
import com.paymentMicroservice.exception.*;
import com.paymentMicroservice.service.impl.PaymentServiceImpl;
import com.paymentMicroservice.validator.PaymentValidator;
import com.stripe.model.*;
import com.stripe.exception.StripeException;
import com.stripe.param.PaymentIntentCancelParams;
import com.stripe.param.PaymentIntentConfirmParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, controlledShutdown = true)
@EnableKafka
public class PaymentServiceImplTest {

    @Mock
    private PaymentValidator paymentValidator;

    @Mock
    private KafkaTemplate<String, RefundException> kafkaRefundNotPossibleExceptionTemplate;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentActionRequestDTO paymentActionRequest;

    private RefundChargeRequestDTO refundChargeRequest;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        paymentActionRequest = new PaymentActionRequestDTO();
        paymentActionRequest.setPaymentIntentId("pi_test");
        refundChargeRequest = new RefundChargeRequestDTO();
        refundChargeRequest.setPaymentIntentId("pi_test");
    }

    @Test
    void testConfirmPaymentIntent_Success() throws Exception {
        PaymentIntent mockPaymentIntent = mock(PaymentIntent.class);
        when(mockPaymentIntent.getStatus()).thenReturn("requires_confirmation");
        when(mockPaymentIntent.confirm(any(PaymentIntentConfirmParams.class))).thenReturn(mockPaymentIntent);
        MockedStatic<PaymentIntent> paymentIntentMockedStatic = Mockito.mockStatic(PaymentIntent.class);
        paymentIntentMockedStatic.when(() -> PaymentIntent.retrieve("pi_test")).thenReturn(mockPaymentIntent);

        paymentService.confirmPaymentIntent(paymentActionRequest);

        verify(mockPaymentIntent).confirm(any(PaymentIntentConfirmParams.class));
        paymentIntentMockedStatic.close();
    }

    @Test
    void testConfirmPaymentIntent_ShouldThrowException() {
        PaymentIntent mockPaymentIntent = mock(PaymentIntent.class);
        when(mockPaymentIntent.getStatus()).thenReturn("canceled");
        MockedStatic<PaymentIntent> paymentIntentMockedStatic = Mockito.mockStatic(PaymentIntent.class);
        paymentIntentMockedStatic.when(() -> PaymentIntent.retrieve("pi_test")).thenReturn(mockPaymentIntent);

        assertThrows(FailedToConfirmPaymentException.class, () -> paymentService.confirmPaymentIntent(paymentActionRequest));

        paymentIntentMockedStatic.close();
    }

    @Test
    void cancelPaymentIntent_Successful() throws StripeException, FailedToCancelPaymentException {
        try (MockedStatic<PaymentIntent> mockedPaymentIntent = mockStatic(PaymentIntent.class)) {
            PaymentIntent mockPaymentIntent = mock(PaymentIntent.class);
            when(mockPaymentIntent.getStatus()).thenReturn("requires_payment_method");
            mockedPaymentIntent.when(() -> PaymentIntent.retrieve("pi_test")).thenReturn(mockPaymentIntent);

            paymentService.cancelPaymentIntent(paymentActionRequest);

            verify(mockPaymentIntent, times(1)).cancel(any(PaymentIntentCancelParams.class));
            verify(mockPaymentIntent, times(1)).getStatus();
        }
    }

    @Test
    void cancelPaymentIntent_shouldThrowException() {
        try (MockedStatic<PaymentIntent> paymentIntentMock = mockStatic(PaymentIntent.class)) {
            PaymentIntent mockPaymentIntent = mock(PaymentIntent.class);
            when(mockPaymentIntent.getStatus()).thenReturn("succeeded");
            paymentIntentMock.when(() -> PaymentIntent.retrieve("pi_test")).thenReturn(mockPaymentIntent);

            assertThrows(FailedToCancelPaymentException.class, () -> paymentService.cancelPaymentIntent(paymentActionRequest));
        }
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

    @Test
    void createSepaToken_Success() throws StripeException, StripeSepaTokenCreationException {
        StripeSepaTokenDTO model = new StripeSepaTokenDTO();
        model.setIban("DE89370400440532013000");
        model.setAccountHolderName("John Doe");
        model.setAccountHolderType("individual");

        Token mockToken = mock(Token.class);
        when(mockToken.getId()).thenReturn("tok_sepa_123");

        MockedStatic<Token> tokenMockedStatic = Mockito.mockStatic(Token.class);
        tokenMockedStatic.when(() -> Token.create(anyMap())).thenReturn(mockToken);

        paymentService.createSepaToken(model);

        assertTrue(model.isSuccess());
        assertEquals("tok_sepa_123", model.getToken());

        tokenMockedStatic.close();
    }

    @Test
    void refundCharge_Successful() throws Exception {
        PaymentIntent mockPaymentIntent = mock(PaymentIntent.class);
        when(mockPaymentIntent.getLatestCharge()).thenReturn("charge_test");

        Charge mockCharge = mock(Charge.class);
        when(mockCharge.getStatus()).thenReturn("succeeded");
        when(mockCharge.getId()).thenReturn("charge_test_id");

        MockedStatic<PaymentIntent> paymentIntentMockedStatic = Mockito.mockStatic(PaymentIntent.class);
        paymentIntentMockedStatic.when(() -> PaymentIntent.retrieve("pi_test")).thenReturn(mockPaymentIntent);

        MockedStatic<Charge> chargeMockedStatic = Mockito.mockStatic(Charge.class);
        chargeMockedStatic.when(() -> Charge.retrieve("charge_test")).thenReturn(mockCharge);

        MockedStatic<Refund> refundMockedStatic = Mockito.mockStatic(Refund.class);
        refundMockedStatic.when(() -> Refund.create(anyMap())).thenReturn(null);

        paymentService.refundCharge(refundChargeRequest);

        paymentIntentMockedStatic.close();
        chargeMockedStatic.close();
        refundMockedStatic.close();
    }

    @Test
    void refundCharge_shouldThrowException() {
        PaymentIntent mockPaymentIntent = mock(PaymentIntent.class);
        when(mockPaymentIntent.getLatestCharge()).thenReturn("charge_test");

        Charge mockCharge = mock(Charge.class);
        when(mockCharge.getStatus()).thenReturn("failed");

        MockedStatic<PaymentIntent> paymentIntentMockedStatic = Mockito.mockStatic(PaymentIntent.class);
        paymentIntentMockedStatic.when(() -> PaymentIntent.retrieve("pi_test")).thenReturn(mockPaymentIntent);

        MockedStatic<Charge> chargeMockedStatic = Mockito.mockStatic(Charge.class);
        chargeMockedStatic.when(() -> Charge.retrieve("charge_test")).thenReturn(mockCharge);

        when(kafkaRefundNotPossibleExceptionTemplate.send(anyString(), any(RefundException.class)))
                .thenReturn(null);

        assertThrows(RefundException.class, () -> paymentService.refundCharge(refundChargeRequest));

        paymentIntentMockedStatic.close();
        chargeMockedStatic.close();
    }

    @Test
    void testCreatePaymentSession_Success() throws Exception {
        SessionRequestDTO sessionRequestDTO = new SessionRequestDTO();
        sessionRequestDTO.setCorrelationId(UUID.randomUUID());
        sessionRequestDTO.setCurrency("eur");
        sessionRequestDTO.setDescription("Test payment");
        sessionRequestDTO.setEmail("test@example.com");

        CreatePaymentItemDTO item = new CreatePaymentItemDTO();
        item.setAmount(100L);
        item.setBudgetSubtypeId(UUID.randomUUID());
        item.setSupplierId(UUID.randomUUID());
        item.setId("fake-id");
        sessionRequestDTO.setItems(new CreatePaymentItemDTO[]{item});

        doNothing().when(paymentValidator).validateFundsForPayment(any(), any());

        assertDoesNotThrow(() -> paymentService.createPaymentSession(sessionRequestDTO));

        doNothing().when(paymentValidator).validateFundsForPayment(any(), any());
    }
}