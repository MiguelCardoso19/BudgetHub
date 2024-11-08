package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import com.portalMicroservice.exception.GenericException;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface BudgetSubtypeService {
    BudgetSubtypeDTO addSubtypeToBudget(BudgetSubtypeDTO budgetSubTypeDTO) throws ExecutionException, InterruptedException, GenericException;
    BudgetSubtypeDTO update(BudgetSubtypeDTO budgetSubTypeDTO) throws ExecutionException, InterruptedException, GenericException;
    void delete(UUID id);
    BudgetSubtypeDTO getById(UUID id) throws GenericException;
}
