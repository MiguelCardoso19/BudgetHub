package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.GenericException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface BudgetTypeService {
    BudgetTypeDTO create(BudgetTypeDTO budgetTypeDTO) throws ExecutionException, InterruptedException, GenericException;
    BudgetTypeDTO update(BudgetTypeDTO budgetTypeDTO) throws ExecutionException, InterruptedException, GenericException;
    void delete(UUID id);
    BudgetTypeDTO getById(UUID id) throws GenericException;
    CustomPageDTO findAll(Pageable pageable) throws GenericException;
}
