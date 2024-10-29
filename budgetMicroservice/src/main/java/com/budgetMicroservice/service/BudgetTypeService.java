package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.BudgetTypeDTO;
import com.budgetMicroservice.exception.BudgetTypeNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BudgetTypeService {
    BudgetTypeDTO createBudgetType(BudgetTypeDTO budgetTypeDTO);
    BudgetTypeDTO updateBudgetType(BudgetTypeDTO budgetTypeDTO) throws BudgetTypeNotFoundException;
    void deleteBudgetType(UUID id) throws BudgetTypeNotFoundException;
    BudgetTypeDTO getBudgetTotalValue(UUID id) throws BudgetTypeNotFoundException;
    BudgetTypeDTO findBudgetTypeById(UUID id) throws BudgetTypeNotFoundException;
    Page<BudgetTypeDTO> findAllBudgetTypes(Pageable pageable);
}
