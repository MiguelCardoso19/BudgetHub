package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.dto.budget.InvoiceDTO;
import com.portalMicroservice.exception.budget.FailedToUploadFileException;
import com.portalMicroservice.exception.budget.InvoiceNotFoundException;
import com.portalMicroservice.service.impl.InvoiceEventListenerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class InvoiceEventListenerServiceImplTest {

    @InjectMocks
    private InvoiceEventListenerServiceImpl eventListenerService;

    @Mock
    private InvoiceService invoiceService;

    private UUID id;

    private UUID correlationId;

    private InvoiceDTO invoiceDTO;

    @BeforeEach
    void setUp() {
        openMocks(this);
        id = UUID.randomUUID();
        invoiceDTO = new InvoiceDTO();
        invoiceDTO.setCorrelationId(correlationId);
        invoiceDTO.setId(id);
    }

    @Test
    void testHandleInvoiceResponse() {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        when(invoiceService.removePendingRequestById(correlationId, invoiceDTO.getId())).thenReturn(future);

        eventListenerService.handleInvoiceResponse(invoiceDTO);

        verify(invoiceService).removePendingRequestById(correlationId, invoiceDTO.getId());
        assertTrue(future.isDone());
        assertEquals(invoiceDTO, future.getNow(null));
    }

    @Test
    void testHandleInvoicePageResponse() {
        CustomPageDTO customPageDTO = mock(CustomPageDTO.class);
        CustomPageableDTO pageable = mock(CustomPageableDTO.class);

        when(customPageDTO.getPageable()).thenReturn(pageable);
        when(pageable.getCorrelationId()).thenReturn(correlationId);

        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        when(invoiceService.removePendingPageRequestById(correlationId)).thenReturn(future);

        eventListenerService.handleInvoicePageResponse(customPageDTO);

        verify(invoiceService).removePendingPageRequestById(correlationId);
        assertTrue(future.isDone());
        assertEquals(customPageDTO, future.getNow(null));
    }

    @Test
    void testHandleDeleteSuccess() {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        when(invoiceService.removePendingRequestById(id, null)).thenReturn(future);

        eventListenerService.handleDeleteSuccess(id);

        verify(invoiceService).removePendingRequestById(id, null);
        assertTrue(future.isDone());
        assertNull(future.getNow(null));
    }

    @Test
    void testHandleNotFoundExceptionResponse() {
        InvoiceNotFoundException exception = new InvoiceNotFoundException(id.toString());

        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        when(invoiceService.removePendingRequestById(UUID.fromString(exception.getId()), null)).thenReturn(future);

        eventListenerService.handleNotFoundExceptionResponse(exception);

        verify(invoiceService).removePendingRequestById(UUID.fromString(exception.getId()), null);
        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void testHandleUploadFileSuccess() {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        when(invoiceService.removePendingRequestById(id, null)).thenReturn(future);

        eventListenerService.handleUploadFileSuccess(id);

        verify(invoiceService).removePendingRequestById(id, null);
        assertTrue(future.isDone());
        assertNull(future.getNow(null));
    }

    @Test
    void testHandleFailedToUploadFileExceptionResponse() {
        FailedToUploadFileException exception = new FailedToUploadFileException(id);

        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        when(invoiceService.removePendingRequestById(exception.getId(), null)).thenReturn(future);

        eventListenerService.handleFailedToUploadFileExceptionResponse(exception);

        verify(invoiceService).removePendingRequestById(exception.getId(), null);
        assertTrue(future.isCompletedExceptionally());
    }
}