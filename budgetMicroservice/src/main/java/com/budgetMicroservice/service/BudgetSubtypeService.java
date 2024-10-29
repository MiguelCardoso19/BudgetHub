package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.BudgetSubtypeDTO;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.BudgetTypeNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BudgetSubtypeService {
    BudgetSubtypeDTO addSubtypeToBudget(BudgetSubtypeDTO budgetSubtypeDTO) throws BudgetTypeNotFoundException;
    Double getTotalSpentForType(UUID typeId) throws BudgetTypeNotFoundException;
    BudgetSubtypeDTO updateBudgetSubtype(BudgetSubtypeDTO budgetSubtypeDTO) throws BudgetSubtypeNotFoundException;
    void deleteBudgetSubtype(UUID subtypeId) throws BudgetSubtypeNotFoundException;
    BudgetSubtypeDTO findBudgetSubtypeById(UUID subtypeId) throws BudgetSubtypeNotFoundException;
    Page<BudgetSubtypeDTO> findAllBudgetSubtypes(Pageable pageable);
}
