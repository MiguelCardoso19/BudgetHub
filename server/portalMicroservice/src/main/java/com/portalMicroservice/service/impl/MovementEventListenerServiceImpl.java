package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.MovementDTO;
import com.portalMicroservice.exception.budget.*;
import com.portalMicroservice.service.MovementEventListenerService;
import com.portalMicroservice.service.MovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MovementEventListenerServiceImpl implements MovementEventListenerService {
    private final MovementService movementService;

    @Override
    @KafkaListener(topics = "movement-response", groupId = "movement_response_group", concurrency = "10", containerFactory = "movementKafkaListenerContainerFactory")
    public void handleMovementResponse(MovementDTO movementDTO) {
        CompletableFuture<MovementDTO> future = movementService.removePendingRequestById(movementDTO.getCorrelationId(), movementDTO.getId());
        if (future != null) {
            future.complete(movementDTO);
        }
    }

    @Override
    @KafkaListener(topics = "movement-page-response", groupId = "pageable_response_group", concurrency = "10", containerFactory = "customPageKafkaListenerContainerFactory")
    public void handleMovementPageResponse(CustomPageDTO customPageDTO) {
        CompletableFuture<CustomPageDTO> future = movementService.removePendingPageRequestById(customPageDTO.getPageable().getCorrelationId());
        if (future != null) {
            future.complete(customPageDTO);
        }
    }

    @Override
    @KafkaListener(topics = "movement-delete-success-response", groupId = "movement_delete_success_response_group", concurrency = "10")
    public void handleDeleteSuccess(UUID id) {
        CompletableFuture<MovementDTO> future = movementService.removePendingRequestById(id, null);
        if (future != null) {
            future.complete(null);
        }
    }

    @Override
    @KafkaListener(topics = "export-report-success-response", groupId = "export_report_success_response_group", concurrency = "10")
    public void handleExportReportSuccess(UUID id) {
        CompletableFuture<MovementDTO> future = movementService.removePendingRequestById(id, null);
        if (future != null) {
            future.complete(null);
        }
    }

    @Override
    @KafkaListener(topics = "generate-excel-exception-response", groupId = "generate_excel_response_group", concurrency = "10", containerFactory = "generateExcelExceptionKafkaListenerContainerFactory")
    public void handleGenerateExcelExceptionResponse(GenerateExcelException errorPayload) {
        CompletableFuture<MovementDTO> future = movementService.removePendingRequestById(errorPayload.getCorrelationId(), null);
        if (future != null) {
            future.completeExceptionally(new GenerateExcelException());
        }
    }

    @Override
    @KafkaListener(topics = "movement-not-found-exception-response", groupId = "movement_not_found_response_group", concurrency = "10", containerFactory = "movementNotFoundExceptionKafkaListenerContainerFactory")
    public void handleNotFoundExceptionResponse(MovementNotFoundException errorPayload) {
        CompletableFuture<MovementDTO> future = movementService.removePendingRequestById(UUID.fromString(errorPayload.getId()), null);
        if (future != null) {
            future.completeExceptionally(new MovementNotFoundException(errorPayload.getId()));
        }
    }

    @Override
    @KafkaListener(topics = "movement-validation-exception-response", groupId = "movement_validation_response_group", concurrency = "10", containerFactory = "movementValidationExceptionKafkaListenerContainerFactory")
    public void handleValidationExceptionResponse(MovementValidationException errorPayload) {
        CompletableFuture<MovementDTO> future = movementService.removePendingRequestById(errorPayload.getId(), null);
        if (future != null) {
            String formatedErrorMessage = errorPayload.getMessage().substring(errorPayload.getMessage().indexOf("[") + 1, errorPayload.getMessage().indexOf("]"));
            future.completeExceptionally(new MovementValidationException(formatedErrorMessage));
        }
    }

    @Override
    @KafkaListener(topics = "movement-budget-exceeded-exception-response", groupId = "movement_budget_exceeded_response_group", concurrency = "10", containerFactory = "movementBudgetExceededExceptionKafkaListenerContainerFactory")
    public void handleBudgetExceededExceptionExceptionResponse(BudgetExceededException errorPayload) {
        CompletableFuture<MovementDTO> future = movementService.removePendingRequestById(errorPayload.getId(), null);
        if (future != null) {
            future.completeExceptionally(new MovementValidationException(errorPayload.getMessage()));
        }
    }

    @Override
    @KafkaListener(topics = "movement-not-found-for-budget-type-exception-response", groupId = "movement_not_found_for_budget_type_response_group", concurrency = "10", containerFactory = "movementsNotFoundForBudgetTypeExceptionKafkaListenerContainerFactory")
    public void handleMovementsNotFoundForBudgetTypeExceptionResponse(MovementsNotFoundForBudgetTypeException errorPayload) {
        CompletableFuture<CustomPageDTO> future = movementService.removePendingPageRequestById(UUID.fromString(errorPayload.getCorrelationId()));
        if (future != null) {
            future.completeExceptionally(new MovementsNotFoundForBudgetTypeException(errorPayload.getBudgetId()));
        }
    }

    @Override
    @KafkaListener(topics = "movement-not-found-for-budget-subtype-exception-response", groupId = "movement_not_found_for_budget_subtype_response_group", concurrency = "10", containerFactory = "movementsNotFoundForBudgetSubtypeExceptionKafkaListenerContainerFactory")
    public void handleMovementsNotFoundForBudgetSubtypeExceptionResponse(MovementsNotFoundForBudgetSubtypeException errorPayload) {
        CompletableFuture<CustomPageDTO> future = movementService.removePendingPageRequestById(UUID.fromString(errorPayload.getCorrelationId()));
        if (future != null) {
            future.completeExceptionally(new MovementsNotFoundForBudgetSubtypeException(errorPayload.getBudgetId()));
        }
    }
}
