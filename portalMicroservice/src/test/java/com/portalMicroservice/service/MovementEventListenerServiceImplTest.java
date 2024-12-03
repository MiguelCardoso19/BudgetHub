package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.dto.budget.MovementDTO;
import com.portalMicroservice.exception.budget.*;
import com.portalMicroservice.service.impl.MovementEventListenerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class MovementEventListenerServiceImplTest {

    @InjectMocks
    private MovementEventListenerServiceImpl eventListenerService;

    @Mock
    private MovementService movementService;

    private UUID id;

    private UUID correlationId;

    @BeforeEach
    void setUp() {
        openMocks(this);
        correlationId = UUID.randomUUID();
        id = UUID.randomUUID();
    }

    @Test
    void testHandleMovementResponse() {
        MovementDTO movementDTO = mock(MovementDTO.class);
        when(movementDTO.getCorrelationId()).thenReturn(correlationId);

        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        when(movementService.removePendingRequestById(correlationId, movementDTO.getId())).thenReturn(future);

        eventListenerService.handleMovementResponse(movementDTO);

        verify(movementService).removePendingRequestById(correlationId, movementDTO.getId());
        assertTrue(future.isDone());
        assertEquals(movementDTO, future.getNow(null));
    }

    @Test
    void testHandleMovementPageResponse() {
        CustomPageDTO customPageDTO = mock(CustomPageDTO.class);
        CustomPageableDTO pageable = mock(CustomPageableDTO.class);

        when(customPageDTO.getPageable()).thenReturn(pageable);
        when(pageable.getCorrelationId()).thenReturn(correlationId);

        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        when(movementService.removePendingPageRequestById(correlationId)).thenReturn(future);

        eventListenerService.handleMovementPageResponse(customPageDTO);

        verify(movementService).removePendingPageRequestById(correlationId);
        assertTrue(future.isDone());
        assertEquals(customPageDTO, future.getNow(null));
    }

    @Test
    void testHandleDeleteSuccess() {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        when(movementService.removePendingRequestById(id, null)).thenReturn(future);

        eventListenerService.handleDeleteSuccess(id);

        verify(movementService).removePendingRequestById(id, null);
        assertTrue(future.isDone());
        assertNull(future.getNow(null));
    }

    @Test
    void testHandleExportReportSuccess() {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        when(movementService.removePendingRequestById(id, null)).thenReturn(future);

        eventListenerService.handleExportReportSuccess(id);

        verify(movementService).removePendingRequestById(id, null);
        assertTrue(future.isDone());
        assertNull(future.getNow(null));
    }

    @Test
    void testHandleGenerateExcelExceptionResponse() {
        GenerateExcelException exception = new GenerateExcelException(correlationId);

        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        when(movementService.removePendingRequestById(exception.getCorrelationId(), null)).thenReturn(future);

        eventListenerService.handleGenerateExcelExceptionResponse(exception);

        verify(movementService).removePendingRequestById(exception.getCorrelationId(), null);
        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void testHandleNotFoundExceptionResponse() {
        MovementNotFoundException exception = new MovementNotFoundException(id.toString());

        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        when(movementService.removePendingRequestById(UUID.fromString(exception.getId()), null)).thenReturn(future);

        eventListenerService.handleNotFoundExceptionResponse(exception);

        verify(movementService).removePendingRequestById(UUID.fromString(exception.getId()), null);
        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void testHandleValidationExceptionResponse() {
        MovementValidationException exception = new MovementValidationException("Error [fakedata]");
        exception.setId(id);

        CompletableFuture<MovementDTO> future = new CompletableFuture<>();

        when(movementService.removePendingRequestById(id, null)).thenReturn(future);

        eventListenerService.handleValidationExceptionResponse(exception);

        verify(movementService).removePendingRequestById(id, null);
        assertTrue(future.isCompletedExceptionally());
    }


    @Test
    void testHandleBudgetExceededExceptionExceptionResponse() {
        BudgetExceededException exception = new BudgetExceededException(id, "fakedata");
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();

        when(movementService.removePendingRequestById(id, null)).thenReturn(future);

        eventListenerService.handleBudgetExceededExceptionExceptionResponse(exception);

        verify(movementService).removePendingRequestById(id, null);
        assertTrue(future.isCompletedExceptionally());
    }


    @Test
    void testHandleMovementsNotFoundForBudgetTypeExceptionResponse() {
        MovementsNotFoundForBudgetTypeException exception =
                new MovementsNotFoundForBudgetTypeException(correlationId.toString(), id.toString());

        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        when(movementService.removePendingPageRequestById(UUID.fromString(exception.getCorrelationId()))).thenReturn(future);

        eventListenerService.handleMovementsNotFoundForBudgetTypeExceptionResponse(exception);

        verify(movementService).removePendingPageRequestById(UUID.fromString(exception.getCorrelationId()));
        assertTrue(future.isCompletedExceptionally());
    }


    @Test
    void testHandleMovementsNotFoundForBudgetSubtypeExceptionResponse() {
        MovementsNotFoundForBudgetSubtypeException exception = new MovementsNotFoundForBudgetSubtypeException(correlationId.toString(), id.toString());

        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        when(movementService.removePendingPageRequestById(UUID.fromString(exception.getCorrelationId()))).thenReturn(future);

        eventListenerService.handleMovementsNotFoundForBudgetSubtypeExceptionResponse(exception);

        verify(movementService).removePendingPageRequestById(UUID.fromString(exception.getCorrelationId()));
        assertTrue(future.isCompletedExceptionally());
    }
}