package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.GenericException;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface BudgetSubtypeService {
    BudgetSubtypeDTO addSubtypeToBudget(BudgetSubtypeDTO budgetSubTypeDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException;
    BudgetSubtypeDTO update(BudgetSubtypeDTO budgetSubTypeDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException;
    void delete(UUID id);
    BudgetSubtypeDTO getById(UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException;
    CustomPageDTO findAll(Pageable pageable) throws GenericException, ExecutionException, InterruptedException, TimeoutException;
    CompletableFuture<BudgetSubtypeDTO> getPendingRequest(UUID correlationId, UUID id);
    CompletableFuture<CustomPageDTO> getPendingPageRequest(UUID correlationId);
}
