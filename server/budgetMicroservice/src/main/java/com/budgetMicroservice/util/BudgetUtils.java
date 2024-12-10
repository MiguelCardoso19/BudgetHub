package com.budgetMicroservice.util;

import com.budgetMicroservice.dto.BudgetSubtypeDTO;
import com.budgetMicroservice.exception.BudgetExceededException;
import com.budgetMicroservice.model.BudgetType;
import com.budgetMicroservice.model.BudgetSubtype;
import com.budgetMicroservice.repository.BudgetSubtypeRepository;
import com.budgetMicroservice.service.BudgetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BudgetUtils {
    private final KafkaTemplate<String, BudgetExceededException> kafkaBudgetExceededExceptionTemplate;

    public void checkBudgetExceeded(BudgetType budgetType, BudgetSubtypeDTO budgetSubtypeDTO, BudgetSubtypeRepository budgetSubtypeRepository, BudgetSubtype existingBudgetSubtype) throws BudgetExceededException {
        double totalSpentForType = budgetSubtypeRepository.findByBudgetType(budgetType).stream()
                .mapToDouble(BudgetSubtype::getAvailableFunds)
                .sum();

        if (existingBudgetSubtype != null) {
            totalSpentForType -= existingBudgetSubtype.getAvailableFunds();
        }

        if (totalSpentForType + budgetSubtypeDTO.getAvailableFunds() > budgetType.getAvailableFunds()) {
            kafkaBudgetExceededExceptionTemplate.send("budget-subtype-budget-exceeded-exception-response", new BudgetExceededException(budgetSubtypeDTO.getCorrelationId(), budgetSubtypeDTO.getAvailableFunds(), totalSpentForType));
            throw new BudgetExceededException(budgetSubtypeDTO.getAvailableFunds(), totalSpentForType);
        }
    }

    public void handleDeleteBudgetSubtypeAvailableFunds(BudgetSubtype budgetSubtype, BudgetTypeService budgetTypeService, BudgetSubtypeRepository budgetSubtypeRepository) {
        BudgetType budgetType = budgetSubtype.getBudgetType();
        budgetType.setAvailableFunds(budgetType.getAvailableFunds() - budgetSubtype.getAvailableFunds());
        budgetTypeService.save(budgetType);
        budgetSubtypeRepository.deleteById(budgetSubtype.getId());
    }
}