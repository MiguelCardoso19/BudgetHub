package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.dto.budget.SupplierDTO;
import com.portalMicroservice.exception.budget.SupplierNotFoundException;
import com.portalMicroservice.exception.budget.SupplierValidationException;
import com.portalMicroservice.service.impl.SupplierEventListenerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class SupplierEventListenerServiceImplTest {

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private SupplierEventListenerServiceImpl supplierEventListenerService;

    private UUID id;

    private UUID correlationId;

    @BeforeEach
    void setUp() {
        openMocks(this);
        id = UUID.randomUUID();
        correlationId = UUID.randomUUID();
    }

    @Test
    void testHandleInvoiceResponse() {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCorrelationId(correlationId);
        supplierDTO.setId(id);

        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();

        when(supplierService.removePendingRequestById(any(UUID.class), any(UUID.class))).thenReturn(future);

        supplierEventListenerService.handleInvoiceResponse(supplierDTO);

        verify(supplierService).removePendingRequestById(any(UUID.class), any(UUID.class));
        future.complete(supplierDTO);
    }

    @Test
    void testHandleInvoicePageResponse() {
        CustomPageableDTO customPageableDTO = new CustomPageableDTO();
        customPageableDTO.setCorrelationId(correlationId);
        CustomPageDTO customPageDTO = new CustomPageDTO();
        customPageDTO.setPageable(customPageableDTO);

        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();

        when(supplierService.removePendingPageRequestById(any(UUID.class))).thenReturn(future);

        supplierEventListenerService.handleInvoicePageResponse(customPageDTO);

        verify(supplierService).removePendingPageRequestById(any(UUID.class));
        future.complete(customPageDTO);
    }

    @Test
    void testHandleDeleteSuccess() {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();

        when(supplierService.removePendingRequestById(eq(id), isNull())).thenReturn(future);

        supplierEventListenerService.handleDeleteSuccess(id);

        verify(supplierService).removePendingRequestById(eq(id), isNull());
        future.complete(null);
    }

    @Test
    void testHandleNotFoundExceptionResponse() {
        SupplierNotFoundException exception = new SupplierNotFoundException(id.toString());
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();

        when(supplierService.removePendingRequestById(any(UUID.class), isNull())).thenReturn(future);

        supplierEventListenerService.handleNotFoundExceptionResponse(exception);

        verify(supplierService).removePendingRequestById(eq(UUID.fromString(exception.getId())), isNull());
        future.completeExceptionally(new SupplierNotFoundException(exception.getId()));
    }

    @Test
    void testHandleValidationExceptionResponse() {
        SupplierValidationException exception = new SupplierValidationException("[Validation failed] Invalid Supplier");
        exception.setId(correlationId);
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();

        when(supplierService.removePendingRequestById(any(UUID.class), isNull())).thenReturn(future);

        supplierEventListenerService.handleValidationExceptionResponse(exception);

        verify(supplierService).removePendingRequestById(eq(correlationId), isNull());
        future.completeExceptionally(new SupplierValidationException("Validation failed"));
    }
}