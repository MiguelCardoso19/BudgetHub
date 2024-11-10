package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.MovementDTO;
import com.portalMicroservice.enumerator.MovementStatus;
import com.portalMicroservice.exception.GenericException;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface MovementService {
    MovementDTO create(MovementDTO movementDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException;
    MovementDTO update(MovementDTO movementDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException;
    void delete(UUID id);
    MovementDTO getById(UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException;
    CustomPageDTO getAll(Pageable pageable) throws GenericException, ExecutionException, InterruptedException, TimeoutException;
    CustomPageDTO getByBudgetType(UUID budgetTypeId, Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException;
    CustomPageDTO getByBudgetSubtype(UUID budgetSubtypeId, Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException;
    void exportMovementsReport(LocalDate startDate, LocalDate endDate, MovementStatus status, String emailFromRequest);
    CompletableFuture<MovementDTO> getPendingRequest(UUID correlationId, UUID id);
    CompletableFuture<CustomPageDTO> getPendingPageRequest(UUID correlationId);
}
