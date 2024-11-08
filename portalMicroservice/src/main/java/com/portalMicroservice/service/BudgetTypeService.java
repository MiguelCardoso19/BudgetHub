package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import com.portalMicroservice.exception.GenericException;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface BudgetTypeService {
    BudgetTypeDTO create(BudgetTypeDTO budgetTypeDTO) throws ExecutionException, InterruptedException, GenericException;
    BudgetTypeDTO update(BudgetTypeDTO budgetTypeDTO) throws ExecutionException, InterruptedException, GenericException;
    void delete(UUID id);
    BudgetTypeDTO getById(UUID id) throws GenericException;
}
