package com.budgetMicroservice.util;

import com.budgetMicroservice.dto.BudgetSubtypeDTO;
import com.budgetMicroservice.exception.BudgetExceededException;
import com.budgetMicroservice.model.BudgetType;
import com.budgetMicroservice.model.BudgetSubtype;
import com.budgetMicroservice.repository.BudgetSubtypeRepository;

public class BudgetUtils {

    public static void checkBudgetExceeded(BudgetType budgetType, BudgetSubtypeDTO budgetSubtypeDTO, BudgetSubtypeRepository budgetSubtypeRepository, BudgetSubtype existingBudgetSubtype) throws BudgetExceededException {
        double totalSpentForType = budgetSubtypeRepository.findByBudgetType(budgetType).stream()
                .mapToDouble(BudgetSubtype::getAvailableFunds)
                .sum();

        if (existingBudgetSubtype != null) {
            totalSpentForType -= existingBudgetSubtype.getAvailableFunds();
        }

        if (totalSpentForType + budgetSubtypeDTO.getAvailableFunds() > budgetType.getAvailableFunds()) {
            throw new BudgetExceededException(budgetSubtypeDTO.getAvailableFunds(), totalSpentForType);
        }
    }
}