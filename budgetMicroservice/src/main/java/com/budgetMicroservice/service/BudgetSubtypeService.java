package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.BudgetSubtypeDTO;
import com.budgetMicroservice.dto.CustomPageableDTO;
import com.budgetMicroservice.exception.BudgetExceededException;
import com.budgetMicroservice.exception.BudgetSubtypeAlreadyExistsException;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.BudgetTypeNotFoundException;
import com.budgetMicroservice.model.BudgetSubtype;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BudgetSubtypeService {
    BudgetSubtypeDTO addSubtypeToBudget(BudgetSubtypeDTO budgetSubtypeDTO) throws BudgetTypeNotFoundException, BudgetSubtypeAlreadyExistsException, BudgetSubtypeNotFoundException, BudgetExceededException;
    BudgetSubtypeDTO updateBudgetSubtype(BudgetSubtypeDTO budgetSubtypeDTO) throws BudgetSubtypeNotFoundException, BudgetSubtypeAlreadyExistsException, BudgetExceededException;
    void deleteBudgetSubtype(UUID subtypeId) throws BudgetSubtypeNotFoundException;
    BudgetSubtypeDTO findBudgetSubtypeDTOById(UUID subtypeId) throws BudgetSubtypeNotFoundException;
    Page<BudgetSubtypeDTO> findAllBudgetSubtypes(CustomPageableDTO customPageableDTO) throws JsonProcessingException;
    BudgetSubtype findBudgetSubtypeEntityById(UUID subtypeId) throws BudgetSubtypeNotFoundException;
    void save(BudgetSubtype subtype);
}
