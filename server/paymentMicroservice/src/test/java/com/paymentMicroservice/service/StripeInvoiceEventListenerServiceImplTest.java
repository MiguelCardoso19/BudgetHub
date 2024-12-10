package com.paymentMicroservice.service;

import com.paymentMicroservice.dto.MovementDTO;
import com.paymentMicroservice.service.impl.StripeInvoiceEventListenerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class StripeInvoiceEventListenerServiceImplTest {

    @Mock
    private StripeInvoiceService stripeInvoiceService;

    @InjectMocks
    private StripeInvoiceEventListenerServiceImpl stripeInvoiceEventListenerService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testHandleMovementResponse() {
        MovementDTO movementDTO = mock(MovementDTO.class);
        CompletableFuture<MovementDTO> future = mock(CompletableFuture.class);
        when(stripeInvoiceService.removePendingMovementRequestByDocumentNumber(movementDTO.getDocumentNumber())).thenReturn(future);

        stripeInvoiceEventListenerService.handleMovementResponse(movementDTO);

        verify(stripeInvoiceService, times(1)).removePendingMovementRequestByDocumentNumber(movementDTO.getDocumentNumber());
        verify(future, times(1)).complete(movementDTO);
    }

    @Test
    void testHandleMovementResponse_withNullFuture() {
        MovementDTO movementDTO = mock(MovementDTO.class);
        when(stripeInvoiceService.removePendingMovementRequestByDocumentNumber(movementDTO.getDocumentNumber())).thenReturn(null);

        stripeInvoiceEventListenerService.handleMovementResponse(movementDTO);

        verify(stripeInvoiceService, times(1)).removePendingMovementRequestByDocumentNumber(movementDTO.getDocumentNumber());
    }
}
