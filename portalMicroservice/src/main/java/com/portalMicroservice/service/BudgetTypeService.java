package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.GenericException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface BudgetTypeService {
    BudgetTypeDTO create(BudgetTypeDTO budgetTypeDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException;
    BudgetTypeDTO update(BudgetTypeDTO budgetTypeDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException;
    void delete(UUID id);
    BudgetTypeDTO getById(UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException;
    CustomPageDTO findAll(Pageable pageable) throws GenericException, ExecutionException, InterruptedException, TimeoutException;
    CompletableFuture<BudgetTypeDTO> getPendingRequest(UUID correlationId, UUID id);
    CompletableFuture<CustomPageDTO> getPendingPageRequest(UUID correlationId);
}
