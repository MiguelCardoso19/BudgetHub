package com.budgetMicroservice.validator;

import com.budgetMicroservice.dto.BudgetSubtypeDTO;
import com.budgetMicroservice.dto.BudgetTypeDTO;
import com.budgetMicroservice.exception.BudgetSubtypeAlreadyExistsException;
import com.budgetMicroservice.exception.BudgetTypeAlreadyExistsException;
import com.budgetMicroservice.repository.BudgetSubtypeRepository;
import com.budgetMicroservice.repository.BudgetTypeRepository;

public class BudgetValidator {

    public static void checkForExistingBudgetType(BudgetTypeDTO budgetTypeDTO,
                                                  BudgetTypeRepository budgetTypeRepository)
            throws BudgetTypeAlreadyExistsException {
        if (budgetTypeRepository.findByName(budgetTypeDTO.getName()).isPresent()) {
            throw new BudgetTypeAlreadyExistsException(budgetTypeDTO.getName());
        }
    }

    public static void checkForExistingBudgetTypeUpdate(BudgetTypeDTO budgetTypeDTO,
                                                        BudgetTypeRepository budgetTypeRepository)
            throws BudgetTypeAlreadyExistsException {
        if (budgetTypeRepository.findByNameAndIdNot(budgetTypeDTO.getName(), budgetTypeDTO.getId()).isPresent()) {
            throw new BudgetTypeAlreadyExistsException(budgetTypeDTO.getName());
        }
    }

    public static void checkForExistingBudgetSubtype(BudgetSubtypeDTO budgetSubtypeDTO,
                                                     BudgetSubtypeRepository budgetSubtypeRepository)
            throws BudgetSubtypeAlreadyExistsException {
        if (budgetSubtypeRepository.findByName(budgetSubtypeDTO.getName()).isPresent()) {
            throw new BudgetSubtypeAlreadyExistsException(budgetSubtypeDTO.getName());
        }
    }

    public static void checkForExistingBudgetSubtypeUpdate(BudgetSubtypeDTO budgetSubtypeDTO,
                                                           BudgetSubtypeRepository budgetSubtypeRepository)
            throws BudgetSubtypeAlreadyExistsException {
        if (budgetSubtypeRepository.findByNameAndIdNot(budgetSubtypeDTO.getName(), budgetSubtypeDTO.getId()).isPresent()) {
            throw new BudgetSubtypeAlreadyExistsException(budgetSubtypeDTO.getName());
        }
    }
}
