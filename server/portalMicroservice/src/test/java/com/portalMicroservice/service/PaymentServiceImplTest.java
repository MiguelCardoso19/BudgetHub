package com.portalMicroservice.service;

import com.portalMicroservice.dto.payment.*;
import com.portalMicroservice.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private KafkaTemplate<String, CreatePaymentDTO> kafkaCreatePaymentTemplate;

    @Mock
    private KafkaTemplate<String, PaymentActionRequestDTO> kafkaPaymentActionTemplate;

    @Mock
    private KafkaTemplate<String, RefundChargeRequestDTO> kafkaRefundTemplate;

    @Mock
    private KafkaTemplate<String, StripeCardTokenDTO> kafkaStripeCardTokenTemplate;

    @Mock
    private KafkaTemplate<String, StripeSepaTokenDTO> kafkaStripeSepaTokenTemplate;

    @Mock
    private KafkaTemplate<String, SessionRequestDTO> kafkaSessionRequestTemplate;

    @Mock
    private ConcurrentHashMap<UUID, CompletableFuture<CreatePaymentResponseDTO>> pendingCreatePaymentIntentRequests;

    @Mock
    private ConcurrentHashMap<UUID, CompletableFuture<SessionResponseDTO>> pendingCreatePaymentSessionRequests;

    @Mock
    private ConcurrentHashMap<String, CompletableFuture<PaymentActionRequestDTO>> pendingActionRequests;

    @Mock
    private ConcurrentHashMap<String, CompletableFuture<RefundChargeRequestDTO>> pendingRefundRequests;

    @Mock
    private ConcurrentHashMap<UUID, CompletableFuture<StripeCardTokenDTO>> pendingCardTokenRequests;

    @Mock
    private ConcurrentHashMap<UUID, CompletableFuture<StripeSepaTokenDTO>> pendingSepaTokenRequests;

    private UUID correlationId;

    private UUID paymentIntentId;

    private CreatePaymentDTO createPaymentDTO;

    private PaymentActionRequestDTO paymentActionRequestDTO;

    private RefundChargeRequestDTO refundChargeRequestDTO;

    private StripeCardTokenDTO stripeCardTokenDTO;

    private StripeSepaTokenDTO stripeSepaTokenDTO;

    private SessionRequestDTO sessionRequestDTO;

    private long timeoutDuration;

    private String email;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        openMocks(this);

        correlationId = UUID.randomUUID();
        paymentIntentId = UUID.randomUUID();
        createPaymentDTO = new CreatePaymentDTO();
        createPaymentDTO.setCorrelationId(correlationId);
        email = "test@example.com";

        paymentActionRequestDTO = new PaymentActionRequestDTO();
        paymentActionRequestDTO.setPaymentIntentId(String.valueOf(paymentIntentId));

        refundChargeRequestDTO = new RefundChargeRequestDTO();
        refundChargeRequestDTO.setPaymentIntentId(String.valueOf(paymentIntentId));

        stripeCardTokenDTO = new StripeCardTokenDTO();
        stripeCardTokenDTO.setCorrelationId(correlationId);

        stripeSepaTokenDTO = new StripeSepaTokenDTO();
        stripeSepaTokenDTO.setCorrelationId(correlationId);

        sessionRequestDTO = new SessionRequestDTO();
        sessionRequestDTO.setCorrelationId(correlationId);

        timeoutDuration = 5L;

        Field timeoutField = PaymentServiceImpl.class.getDeclaredField("TIMEOUT_DURATION");
        timeoutField.setAccessible(true);
        timeoutField.set(paymentService, timeoutDuration);

        Field pendingCreatePaymentIntentRequestsField = PaymentServiceImpl.class.getDeclaredField("pendingCreatePaymentIntentRequests");
        pendingCreatePaymentIntentRequestsField.setAccessible(true);
        pendingCreatePaymentIntentRequests = (ConcurrentHashMap<UUID, CompletableFuture<CreatePaymentResponseDTO>>) pendingCreatePaymentIntentRequestsField.get(paymentService);

        Field pendingCreatePaymentSessionRequestsField = PaymentServiceImpl.class.getDeclaredField("pendingCreatePaymentSessionRequests");
        pendingCreatePaymentSessionRequestsField.setAccessible(true);
        pendingCreatePaymentSessionRequests = (ConcurrentHashMap<UUID, CompletableFuture<SessionResponseDTO>>) pendingCreatePaymentSessionRequestsField.get(paymentService);

        Field pendingActionRequestsField = PaymentServiceImpl.class.getDeclaredField("pendingActionRequests");
        pendingActionRequestsField.setAccessible(true);
        pendingActionRequests = (ConcurrentHashMap<String, CompletableFuture<PaymentActionRequestDTO>>) pendingActionRequestsField.get(paymentService);

        Field pendingRefundRequestsField = PaymentServiceImpl.class.getDeclaredField("pendingRefundRequests");
        pendingRefundRequestsField.setAccessible(true);
        pendingRefundRequests = (ConcurrentHashMap<String, CompletableFuture<RefundChargeRequestDTO>>) pendingRefundRequestsField.get(paymentService);

        Field pendingCardTokenRequestsField = PaymentServiceImpl.class.getDeclaredField("pendingCardTokenRequests");
        pendingCardTokenRequestsField.setAccessible(true);
        pendingCardTokenRequests = (ConcurrentHashMap<UUID, CompletableFuture<StripeCardTokenDTO>>) pendingCardTokenRequestsField.get(paymentService);

        Field pendingSepaTokenRequestsField = PaymentServiceImpl.class.getDeclaredField("pendingSepaTokenRequests");
        pendingSepaTokenRequestsField.setAccessible(true);
        pendingSepaTokenRequests = (ConcurrentHashMap<UUID, CompletableFuture<StripeSepaTokenDTO>>) pendingSepaTokenRequestsField.get(paymentService);
    }

    @Test
    public void createPaymentIntent_Success() throws Exception {
        CompletableFuture<CreatePaymentResponseDTO> future = new CompletableFuture<>();
        CreatePaymentResponseDTO responseDTO = new CreatePaymentResponseDTO(paymentIntentId.toString(), correlationId);
        future.complete(responseDTO);
        pendingCreatePaymentIntentRequests.put(createPaymentDTO.getCorrelationId(), future);

        PaymentServiceImpl paymentServiceMock = mock(PaymentServiceImpl.class);
        when(paymentServiceMock.createPaymentIntent(createPaymentDTO, email)).thenReturn(future.get());

        assertEquals(responseDTO, paymentServiceMock.createPaymentIntent(createPaymentDTO, email));
    }

    @Test
    void createPaymentIntent_NoResponse() {
        CompletableFuture<CreatePaymentResponseDTO> future = new CompletableFuture<>();
        pendingCreatePaymentIntentRequests.put(createPaymentDTO.getCorrelationId(), future);

        assertThatThrownBy(() -> paymentService.createPaymentIntent(createPaymentDTO, email))
                .isInstanceOf(TimeoutException.class);
    }

    @Test
    public void cancelPayment_Success() throws Exception {
        CompletableFuture<PaymentActionRequestDTO> future = new CompletableFuture<>();
        future.complete(paymentActionRequestDTO);
        pendingActionRequests.put(paymentActionRequestDTO.getPaymentIntentId(), future);

        PaymentServiceImpl paymentServiceMock = mock(PaymentServiceImpl.class);
        doNothing().when(paymentServiceMock).cancelPayment(paymentActionRequestDTO, email);

        paymentServiceMock.cancelPayment(paymentActionRequestDTO, email);

        verify(paymentServiceMock).cancelPayment(paymentActionRequestDTO, email);
    }

    @Test
    void cancelPayment_NoResponse() {
        CompletableFuture<PaymentActionRequestDTO> future = new CompletableFuture<>();
        pendingActionRequests.put(paymentActionRequestDTO.getPaymentIntentId(), future);

        assertThatThrownBy(() -> paymentService.cancelPayment(paymentActionRequestDTO, email))
                .isInstanceOf(TimeoutException.class);
    }

    @Test
    public void refundCharge_Success() throws Exception {
        CompletableFuture<RefundChargeRequestDTO> future = new CompletableFuture<>();
        future.complete(refundChargeRequestDTO);
        pendingRefundRequests.put(refundChargeRequestDTO.getPaymentIntentId(), future);

        PaymentServiceImpl paymentServiceMock = mock(PaymentServiceImpl.class);
        doNothing().when(paymentServiceMock).refundCharge(refundChargeRequestDTO, email);

        paymentServiceMock.refundCharge(refundChargeRequestDTO, email);

        verify(paymentServiceMock).refundCharge(refundChargeRequestDTO, email);
    }

    @Test
    void refundCharge_NoResponse() {
        CompletableFuture<RefundChargeRequestDTO> future = new CompletableFuture<>();
        pendingRefundRequests.put(refundChargeRequestDTO.getPaymentIntentId(), future);

        assertThatThrownBy(() -> paymentService.refundCharge(refundChargeRequestDTO, email)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void createCardToken_Success() throws Exception {
        CompletableFuture<StripeCardTokenDTO> future = new CompletableFuture<>();
        future.complete(stripeCardTokenDTO);
        pendingCardTokenRequests.put(stripeCardTokenDTO.getCorrelationId(), future);

        PaymentServiceImpl paymentServiceMock = mock(PaymentServiceImpl.class);
        when(paymentServiceMock.createCardToken(stripeCardTokenDTO, email)).thenReturn(future.get());

        assertEquals(stripeCardTokenDTO, paymentServiceMock.createCardToken(stripeCardTokenDTO, email));
    }

    @Test
    void createCardToken_NoResponse() {
        CompletableFuture<StripeCardTokenDTO> future = new CompletableFuture<>();
        pendingCardTokenRequests.put(stripeCardTokenDTO.getCorrelationId(), future);

        assertThatThrownBy(() -> paymentService.createCardToken(stripeCardTokenDTO, email)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void createSepaToken_Success() throws Exception {
        CompletableFuture<StripeSepaTokenDTO> future = new CompletableFuture<>();
        future.complete(stripeSepaTokenDTO);
        pendingSepaTokenRequests.put(stripeSepaTokenDTO.getCorrelationId(), future);

        PaymentServiceImpl paymentServiceMock = mock(PaymentServiceImpl.class);
        when(paymentServiceMock.createSepaToken(stripeSepaTokenDTO, email)).thenReturn(future.get());

        assertEquals(stripeSepaTokenDTO, paymentServiceMock.createSepaToken(stripeSepaTokenDTO, email));
    }

    @Test
    void createSepaToken_NoResponse() {
        CompletableFuture<StripeSepaTokenDTO> future = new CompletableFuture<>();
        pendingSepaTokenRequests.put(stripeSepaTokenDTO.getCorrelationId(), future);

        assertThatThrownBy(() -> paymentService.createSepaToken(stripeSepaTokenDTO, email)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void createSession_Success() throws Exception {
        CompletableFuture<SessionResponseDTO> future = new CompletableFuture<>();
        SessionResponseDTO sessionResponseDTO = new SessionResponseDTO();
        future.complete(sessionResponseDTO);
        pendingCreatePaymentSessionRequests.put(sessionRequestDTO.getCorrelationId(), future);

        PaymentServiceImpl paymentServiceMock = mock(PaymentServiceImpl.class);
        when(paymentServiceMock.createPaymentSession(sessionRequestDTO, email)).thenReturn(future.get());

        assertEquals(sessionResponseDTO, paymentServiceMock.createPaymentSession(sessionRequestDTO, email));
    }

    @Test
    void createSession_NoResponse() {
        CompletableFuture<SessionResponseDTO> future = new CompletableFuture<>();
        pendingCreatePaymentSessionRequests.put(sessionRequestDTO.getCorrelationId(), future);

        assertThatThrownBy(() -> paymentService.createPaymentSession(sessionRequestDTO, email)).isInstanceOf(TimeoutException.class);
    }

    @Test
    void removePendingCreatePaymentIntentRequestByCorrelationId_Success() {
        CompletableFuture<CreatePaymentResponseDTO> future = new CompletableFuture<>();
        pendingCreatePaymentIntentRequests.put(correlationId, future);

        assertThat(paymentService.removePendingCreatePaymentIntentRequestByCorrelationId(correlationId)).isEqualTo(future);
        assertThat(pendingCreatePaymentIntentRequests).doesNotContainKey(correlationId);
    }

    @Test
    void removePendingActionRequestByIntentId_Success() {
        CompletableFuture<PaymentActionRequestDTO> future = new CompletableFuture<>();
        pendingActionRequests.put(correlationId.toString(), future);

        assertThat(paymentService.removePendingActionRequestByIntentId(correlationId.toString())).isEqualTo(future);
        assertThat(pendingActionRequests).doesNotContainKey(correlationId.toString());
    }

    @Test
    void removePendingRefundRequestByIntentId_Success() {
        CompletableFuture<RefundChargeRequestDTO> future = new CompletableFuture<>();
        pendingRefundRequests.put(correlationId.toString(), future);

        assertThat(paymentService.removePendingRefundRequestByIntentId(correlationId.toString())).isEqualTo(future);
        assertThat(pendingRefundRequests).doesNotContainKey(correlationId.toString());
    }

    @Test
    void removePendingCardTokenRequestByCorrelationId_Success() {
        CompletableFuture<StripeCardTokenDTO> future = new CompletableFuture<>();
        pendingCardTokenRequests.put(correlationId, future);

        assertThat(paymentService.removePendingCardTokenRequestByCorrelationId(correlationId)).isEqualTo(future);
        assertThat(pendingCardTokenRequests).doesNotContainKey(correlationId);
    }

    @Test
    void removePendingSepaTokenRequestByCorrelationId_Success() {
        CompletableFuture<StripeSepaTokenDTO> future = new CompletableFuture<>();
        pendingSepaTokenRequests.put(correlationId, future);

        assertThat(paymentService.removePendingSepaTokenRequestByCorrelationId(correlationId)).isEqualTo(future);
        assertThat(pendingSepaTokenRequests).doesNotContainKey(correlationId);
    }

    @Test
    void removePendingCreatePaymentSessionRequestByCorrelationId_Success() {
        CompletableFuture<SessionResponseDTO> future = new CompletableFuture<>();
        pendingCreatePaymentSessionRequests.put(correlationId, future);

        assertThat(paymentService.removePendingCreatePaymentSessionRequestByCorrelationId(correlationId)).isEqualTo(future);
        assertThat(pendingCreatePaymentSessionRequests).doesNotContainKey(correlationId);
    }
}