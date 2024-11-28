package com.paymentMicroservice.service;

import com.paymentMicroservice.dto.*;
import com.paymentMicroservice.service.impl.StripeInvoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

class StripeInvoiceServiceImplTest {

    @Mock
    private KafkaTemplate<String, NotificationRequestDTO> kafkaNotificationRequestTemplate;

    @Mock
    private KafkaTemplate<String, String> kafkaStringTemplate;

    @InjectMocks
    private StripeInvoiceServiceImpl stripeInvoiceService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    public void testSendChargeSucceededInvoice() {
        String receiptUrl = "https://example.com/receipt";
        String movementDocumentNumber = "MOV12345";
        String email = "customer@example.com";

        stripeInvoiceService.sendChargeSucceededInvoice(receiptUrl, movementDocumentNumber, email);

        verify(kafkaNotificationRequestTemplate, times(1));
    }

    @Test
    void testRemovePendingMovementRequestByDocumentNumber_Success() {
        String documentNumber = "doc123";

        stripeInvoiceService.removePendingMovementRequestByDocumentNumber(documentNumber);

        assertNull(stripeInvoiceService.removePendingMovementRequestByDocumentNumber(documentNumber));
    }

    @Test
    void testSendChargeRefundInvoiceTimeout() {
        String receiptUrl = "http://receipt.url";
        String movementDocumentNumber = "doc123";
        String email = "test@example.com";

        when(kafkaStringTemplate.send(anyString(), eq(movementDocumentNumber), eq(movementDocumentNumber)))
                .thenReturn(null);

        assertThrows(TimeoutException.class, () -> {
            stripeInvoiceService.sendChargeRefundInvoice(receiptUrl, movementDocumentNumber, email);
        });
    }
}