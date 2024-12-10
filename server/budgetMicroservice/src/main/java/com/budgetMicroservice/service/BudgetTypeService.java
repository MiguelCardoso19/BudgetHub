package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.BudgetTypeDTO;
import com.budgetMicroservice.dto.CustomPageableDTO;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.BudgetTypeAlreadyExistsException;
import com.budgetMicroservice.exception.BudgetTypeNotFoundException;
import com.budgetMicroservice.model.BudgetType;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BudgetTypeService {
    BudgetTypeDTO createBudgetType(BudgetTypeDTO budgetTypeDTO) throws BudgetTypeAlreadyExistsException;
    BudgetTypeDTO updateBudgetType(BudgetTypeDTO budgetTypeDTO) throws BudgetTypeNotFoundException, BudgetTypeAlreadyExistsException, BudgetSubtypeNotFoundException;
    void deleteBudgetType(UUID id) throws BudgetTypeNotFoundException;
    BudgetTypeDTO findBudgetTypeDTOById(UUID id) throws BudgetTypeNotFoundException, BudgetSubtypeNotFoundException;
    Page<BudgetTypeDTO> findAllBudgetTypes(CustomPageableDTO customPageableDTO) throws JsonProcessingException;
    BudgetType findBudgetTypeEntityById(UUID id) throws BudgetSubtypeNotFoundException, BudgetTypeNotFoundException;
    void save(BudgetType budgetType);
}
