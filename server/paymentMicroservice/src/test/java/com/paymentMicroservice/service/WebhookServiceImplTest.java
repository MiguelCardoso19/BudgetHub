package com.paymentMicroservice.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

import com.paymentMicroservice.dto.MovementDTO;
import com.paymentMicroservice.service.impl.WebhookServiceImpl;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

public class WebhookServiceImplTest {

    @Mock
    private KafkaTemplate<String, MovementDTO> kafkaMovementTemplate;

    @InjectMocks
    private WebhookServiceImpl webhookService;

    private String payload;

    private String sigHeader;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        payload = "{\"id\":\"evt_1EXAMPLE\"}";
        sigHeader = "t=1234567890,v1=signature";

        Event mockEvent = mock(Event.class);
        when(mockEvent.getType()).thenReturn("payment_intent.created");

        EventDataObjectDeserializer mockDeserializer = mock(EventDataObjectDeserializer.class);
        PaymentIntent mockPaymentIntent = mock(PaymentIntent.class);
        when(mockPaymentIntent.getId()).thenReturn("pi_test");
        when(mockDeserializer.getObject()).thenReturn(java.util.Optional.of(mockPaymentIntent));
        when(mockEvent.getDataObjectDeserializer()).thenReturn(mockDeserializer);
    }

    @Test
    void testHandleWebhookEvents_Success() throws Exception {
        try (MockedStatic<Webhook> webhookMock = mockStatic(Webhook.class)) {
            Event mockEvent = mock(Event.class);
            when(mockEvent.getType()).thenReturn("payment_intent.created");

            EventDataObjectDeserializer mockDeserializer = mock(EventDataObjectDeserializer.class);
            PaymentIntent mockPaymentIntent = mock(PaymentIntent.class);
            when(mockPaymentIntent.getId()).thenReturn("pi_test");

            Map<String, String> metadata = Map.of(
                    "description", "Test payment",
                    "movement_type", "PURCHASE",
                    "iva_rate", "21",
                    "total_amount", "1000"
            );
            when(mockPaymentIntent.getMetadata()).thenReturn(metadata);

            when(mockDeserializer.getObject()).thenReturn(java.util.Optional.of(mockPaymentIntent));
            when(mockEvent.getDataObjectDeserializer()).thenReturn(mockDeserializer);

            webhookMock.when(() -> Webhook.constructEvent(payload, sigHeader, "your_stripe_webhook_key"))
                    .thenReturn(mockEvent);

            assertEquals("",  webhookService.handleWebhookEvents(payload, sigHeader));
        }
    }

    @Test
    void testHandleWebhookEvents_SignatureVerificationFailed() throws Exception {
        sigHeader = "invalid_signature";

        String response = webhookService.handleWebhookEvents(payload, sigHeader);

        verify(kafkaMovementTemplate, times(0)).send(anyString(), anyString(), any(MovementDTO.class));
        assertEquals("", response);
    }

    @Test
    void testHandleWebhookEvents_UnknownEventType() throws Exception {
        Event mockEvent = mock(Event.class);
        when(mockEvent.getType()).thenReturn("unknown.event");

        EventDataObjectDeserializer mockDeserializer = mock(EventDataObjectDeserializer.class);
        when(mockEvent.getDataObjectDeserializer()).thenReturn(mockDeserializer);
        when(mockDeserializer.getObject()).thenReturn(java.util.Optional.empty());

        String response = webhookService.handleWebhookEvents(payload, sigHeader);

        verify(kafkaMovementTemplate, times(0)).send(anyString(), anyString(), any(MovementDTO.class));
        assertEquals("", response);
    }
}