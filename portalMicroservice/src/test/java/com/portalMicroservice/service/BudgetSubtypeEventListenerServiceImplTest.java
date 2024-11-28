package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.exception.budget.*;
import com.portalMicroservice.service.impl.BudgetSubtypeEventListenerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@EmbeddedKafka(partitions = 1, controlledShutdown = true)
public class BudgetSubtypeEventListenerServiceImplTest {

    @Mock
    private BudgetSubtypeService budgetSubtypeService;

    @InjectMocks
    private BudgetSubtypeEventListenerServiceImpl budgetSubtypeEventListenerService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @Test
    void handleBudgetSubtypeResponse_shouldCompleteSuccessfully_whenValidDTO() {
        UUID correlationId = UUID.randomUUID();
        UUID budgetSubtypeId = UUID.randomUUID();
        BudgetSubtypeDTO budgetSubtypeDTO = new BudgetSubtypeDTO();
        budgetSubtypeDTO.setCorrelationId(correlationId);
        budgetSubtypeDTO.setId(budgetSubtypeId);

        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        when(budgetSubtypeService.removePendingRequestById(correlationId, budgetSubtypeId)).thenReturn(future);

        budgetSubtypeEventListenerService.handleBudgetSubtypeResponse(budgetSubtypeDTO);

        verify(budgetSubtypeService, times(1)).removePendingRequestById(correlationId, budgetSubtypeId);
        future.complete(budgetSubtypeDTO);
    }

    @Test
    void handleBudgetSubtypePageResponse_shouldCompleteSuccessfully_whenValidDTO() {
        UUID correlationId = UUID.randomUUID();
        CustomPageDTO customPageDTO = new CustomPageDTO();

        CustomPageableDTO pageableDTO = new CustomPageableDTO();
        pageableDTO.setCorrelationId(correlationId);
        customPageDTO.setPageable(pageableDTO);

        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        when(budgetSubtypeService.removePendingPageRequestById(correlationId)).thenReturn(future);

        budgetSubtypeEventListenerService.handleBudgetSubtypePageResponse(customPageDTO);

        verify(budgetSubtypeService, times(1)).removePendingPageRequestById(correlationId);
        future.complete(customPageDTO);
    }

    @Test
    void handleDeleteSuccess_shouldCompleteSuccessfully_whenValidId() {
        UUID id = UUID.randomUUID();
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        when(budgetSubtypeService.removePendingRequestById(id, null)).thenReturn(future);

        budgetSubtypeEventListenerService.handleDeleteSuccess(id);

        verify(budgetSubtypeService, times(1)).removePendingRequestById(id, null);
        future.complete(null);
    }

    @Test
    void handleNotFoundExceptionResponse_shouldCompleteExceptionally_whenErrorPayloadIsReceived() {
        String errorId = UUID.randomUUID().toString();
        BudgetSubtypeNotFoundException errorPayload = new BudgetSubtypeNotFoundException(errorId);
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        when(budgetSubtypeService.removePendingRequestById(UUID.fromString(errorId), null)).thenReturn(future);

        budgetSubtypeEventListenerService.handleNotFoundExceptionResponse(errorPayload);

        verify(budgetSubtypeService, times(1)).removePendingRequestById(UUID.fromString(errorId), null);
        future.completeExceptionally(new BudgetSubtypeNotFoundException(errorId));
    }

    @Test
    void handleValidationExceptionResponse_shouldCompleteExceptionally_whenErrorPayloadIsReceived() {
        String errorMessage = "Budget subtype already exists";
        BudgetSubtypeAlreadyExistsException errorPayload = new BudgetSubtypeAlreadyExistsException(errorMessage);
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        when(budgetSubtypeService.removePendingRequestById(errorPayload.getId(), null)).thenReturn(future);

        budgetSubtypeEventListenerService.handleValidationExceptionResponse(errorPayload);

        verify(budgetSubtypeService, times(1)).removePendingRequestById(errorPayload.getId(), null);
        future.completeExceptionally(new BudgetSubtypeAlreadyExistsException(extractErrorMessage(errorPayload.getMessage())));
    }

    @Test
    void handleBudgetExceededExceptionResponse_shouldCompleteExceptionally_whenErrorPayloadIsReceived() {
        String errorMessage = "Budget exceeded";
        BudgetExceededException errorPayload = new BudgetExceededException(errorMessage);
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        when(budgetSubtypeService.removePendingRequestById(errorPayload.getId(), null)).thenReturn(future);

        budgetSubtypeEventListenerService.handleBudgetExceededExceptionResponse(errorPayload);

        verify(budgetSubtypeService, times(1)).removePendingRequestById(errorPayload.getId(), null);
        future.completeExceptionally(new BudgetExceededException(errorMessage));
    }

    private String extractErrorMessage(String message) {
        int firstQuoteIndex = message.indexOf("'");
        int secondQuoteIndex = message.indexOf("'", firstQuoteIndex + 1);
        return message.substring(firstQuoteIndex + 1, secondQuoteIndex);
    }
}
