package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.exception.budget.BudgetTypeAlreadyExistsException;
import com.portalMicroservice.exception.budget.BudgetTypeNotFoundException;
import com.portalMicroservice.service.impl.BudgetTypeEventListenerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class BudgetTypeEventListenerServiceImplTest {

    @InjectMocks
    private BudgetTypeEventListenerServiceImpl eventListenerService;

    @Mock
    private BudgetTypeService budgetTypeService;

    private BudgetTypeDTO budgetTypeDTO;

    private UUID id;

    private UUID correlationId;

    @BeforeEach
    void setUp() {
        openMocks(this);
        id = UUID.randomUUID();
        correlationId = UUID.randomUUID();
        budgetTypeDTO = new BudgetTypeDTO();
        budgetTypeDTO.setCorrelationId(correlationId);
        budgetTypeDTO.setId(id);
    }

    @Test
    void testHandleBudgetTypeResponse() {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        when(budgetTypeService.removePendingRequestById(budgetTypeDTO.getCorrelationId(), budgetTypeDTO.getId())).thenReturn(future);

        eventListenerService.handleBudgetTypeResponse(budgetTypeDTO);

        verify(budgetTypeService).removePendingRequestById(budgetTypeDTO.getCorrelationId(), budgetTypeDTO.getId());
        assertTrue(future.isDone());
        assertEquals(budgetTypeDTO, future.getNow(null));
    }

    @Test
    void testHandleBudgetTypePageResponse() {
        CustomPageableDTO pageable = mock(CustomPageableDTO.class);
        when(pageable.getCorrelationId()).thenReturn(correlationId);

        CustomPageDTO customPageDTO = mock(CustomPageDTO.class);
        when(customPageDTO.getPageable()).thenReturn(pageable);

        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        when(budgetTypeService.removePendingPageRequestById(correlationId)).thenReturn(future);

        eventListenerService.handleBudgetTypePageResponse(customPageDTO);

        verify(budgetTypeService).removePendingPageRequestById(correlationId);
        assertTrue(future.isDone());
        assertEquals(customPageDTO, future.getNow(null));
    }

    @Test
    void testHandleDeleteSuccess() {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        when(budgetTypeService.removePendingRequestById(id, null)).thenReturn(future);

        eventListenerService.handleDeleteSuccess(id);

        verify(budgetTypeService).removePendingRequestById(id, null);
        assertTrue(future.isDone());
        assertNull(future.getNow(null));
    }

    @Test
    void testHandleNotFoundExceptionResponse() {
        BudgetTypeNotFoundException exception = new BudgetTypeNotFoundException(id.toString());

        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        when(budgetTypeService.removePendingRequestById(UUID.fromString(exception.getId()), null)).thenReturn(future);

        eventListenerService.handleNotFoundExceptionResponse(exception);

        verify(budgetTypeService).removePendingRequestById(UUID.fromString(exception.getId()), null);
        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void testHandleValidationExceptionResponse() {
        BudgetTypeAlreadyExistsException exception = new BudgetTypeAlreadyExistsException("fakedata");
        exception.setId(id);

        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        when(budgetTypeService.removePendingRequestById(exception.getId(), null)).thenReturn(future);

        eventListenerService.handleValidationExceptionResponse(exception);

        verify(budgetTypeService).removePendingRequestById(exception.getId(), null);
        assertTrue(future.isCompletedExceptionally());
    }
}