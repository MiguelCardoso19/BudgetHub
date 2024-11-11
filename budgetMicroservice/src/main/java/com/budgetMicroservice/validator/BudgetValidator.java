package com.budgetMicroservice.validator;

import com.budgetMicroservice.dto.BudgetSubtypeDTO;
import com.budgetMicroservice.dto.BudgetTypeDTO;
import com.budgetMicroservice.exception.BudgetSubtypeAlreadyExistsException;
import com.budgetMicroservice.exception.BudgetTypeAlreadyExistsException;
import com.budgetMicroservice.repository.BudgetSubtypeRepository;
import com.budgetMicroservice.repository.BudgetTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BudgetValidator {
    private final KafkaTemplate<String, BudgetTypeAlreadyExistsException> kafkaBudgetTypeAlreadyExistsExceptionTemplate;
    private final KafkaTemplate<String, BudgetSubtypeAlreadyExistsException> kafkaBudgetSubtypeAlreadyExistsExceptionTemplate;


    public void checkForExistingBudgetType(BudgetTypeDTO budgetTypeDTO,
                                                  BudgetTypeRepository budgetTypeRepository)
            throws BudgetTypeAlreadyExistsException {
        if (budgetTypeRepository.findByName(budgetTypeDTO.getName()).isPresent()) {
            kafkaBudgetTypeAlreadyExistsExceptionTemplate.send("budget-type-already-exists-exception-response",new BudgetTypeAlreadyExistsException(budgetTypeDTO.getCorrelationId(), budgetTypeDTO.getName()));
            throw new BudgetTypeAlreadyExistsException(budgetTypeDTO.getName());
        }
    }

    public void checkForExistingBudgetTypeUpdate(BudgetTypeDTO budgetTypeDTO,
                                                        BudgetTypeRepository budgetTypeRepository)
            throws BudgetTypeAlreadyExistsException {
        if (budgetTypeRepository.findByNameAndIdNot(budgetTypeDTO.getName(), budgetTypeDTO.getId()).isPresent()) {
            kafkaBudgetTypeAlreadyExistsExceptionTemplate.send("budget-type-already-exists-exception-response",new BudgetTypeAlreadyExistsException(budgetTypeDTO.getCorrelationId(), budgetTypeDTO.getName()));
            throw new BudgetTypeAlreadyExistsException(budgetTypeDTO.getName());
        }
    }

    public void checkForExistingBudgetSubtype(BudgetSubtypeDTO budgetSubtypeDTO,
                                                     BudgetSubtypeRepository budgetSubtypeRepository)
            throws BudgetSubtypeAlreadyExistsException {
        if (budgetSubtypeRepository.findByName(budgetSubtypeDTO.getName()).isPresent()) {
            kafkaBudgetSubtypeAlreadyExistsExceptionTemplate.send("budget-subtype-already-exists-exception-response",new BudgetSubtypeAlreadyExistsException(budgetSubtypeDTO.getCorrelationId(), budgetSubtypeDTO.getName()));
            throw new BudgetSubtypeAlreadyExistsException(budgetSubtypeDTO.getName());
        }
    }

    public void checkForExistingBudgetSubtypeUpdate(BudgetSubtypeDTO budgetSubtypeDTO,
                                                           BudgetSubtypeRepository budgetSubtypeRepository)
            throws BudgetSubtypeAlreadyExistsException {
        if (budgetSubtypeRepository.findByNameAndIdNot(budgetSubtypeDTO.getName(), budgetSubtypeDTO.getId()).isPresent()) {
            kafkaBudgetSubtypeAlreadyExistsExceptionTemplate.send("budget-subtype-already-exists-exception-response",new BudgetSubtypeAlreadyExistsException(budgetSubtypeDTO.getCorrelationId(), budgetSubtypeDTO.getName()));
            throw new BudgetSubtypeAlreadyExistsException(budgetSubtypeDTO.getName());
        }
    }
}
