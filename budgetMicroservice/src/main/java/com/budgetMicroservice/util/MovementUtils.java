package com.budgetMicroservice.util;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.MovementValidationException;
import com.budgetMicroservice.model.BudgetSubtype;
import com.budgetMicroservice.model.BudgetType;
import com.budgetMicroservice.model.Movement;
import com.budgetMicroservice.service.BudgetSubtypeService;
import com.budgetMicroservice.service.BudgetTypeService;
import com.budgetMicroservice.validator.MovementValidator;

public class MovementUtils {

    public static void calculateIvaAndTotal(MovementDTO movementDTO) throws MovementValidationException {
        MovementValidator.validateMovementValues(movementDTO);

        Double ivaValue = (movementDTO.getValueWithoutIva() * (movementDTO.getIvaRate() != null ? movementDTO.getIvaRate() : 0)) / 100;
        Double totalValue = movementDTO.getValueWithoutIva() + ivaValue;

        movementDTO.setIvaValue(ivaValue);
        movementDTO.setTotalValue(totalValue);
    }

    public static void setBudget(Movement movement, MovementDTO movementDTO,
                                 BudgetSubtypeService budgetSubtypeService,
                                 BudgetTypeService budgetTypeService) throws BudgetSubtypeNotFoundException {
        if (movementDTO.getBudgetSubtypeId() != null) {
            BudgetSubtype budgetSubtype = budgetSubtypeService.findBudgetSubtypeEntityById(movementDTO.getBudgetSubtypeId());
            movement.setBudgetSubtype(budgetSubtype);
            movement.setBudgetType(null);
        } else if (movementDTO.getBudgetTypeId() != null) {
            BudgetType budgetType = budgetTypeService.findBudgetTypeEntityById(movementDTO.getBudgetTypeId());
            movement.setBudgetType(budgetType);
            movement.setBudgetSubtype(null);
        }
    }

    public static void updateSpentAmounts(BudgetSubtypeService budgetSubtypeService,
                                          BudgetTypeService budgetTypeService,
                                          Movement movement, Double totalValue) {
        if (movement.getBudgetSubtype() != null) {
            BudgetSubtype subtype = movement.getBudgetSubtype();
            subtype.setTotalSpent(subtype.getTotalSpent() + totalValue);
            budgetSubtypeService.save(subtype);
        } else if (movement.getBudgetType() != null) {
            BudgetType type = movement.getBudgetType();
            type.setTotalSpent(type.getTotalSpent() + totalValue);
            budgetTypeService.save(type);
        }
    }

    public static void adjustBudgetAmounts(BudgetSubtypeService budgetSubtypeService,
                                           BudgetTypeService budgetTypeService,
                                           Movement oldMovement, MovementDTO newMovementDTO) {
        double oldTotalValue = oldMovement.getTotalValue();
        double newTotalValue = newMovementDTO.getTotalValue();

        if (oldMovement.getBudgetSubtype() != null) {
            BudgetSubtype subtype = oldMovement.getBudgetSubtype();
            subtype.setTotalSpent(subtype.getTotalSpent() - oldTotalValue + newTotalValue);
            budgetSubtypeService.save(subtype);
        } else if (oldMovement.getBudgetType() != null) {
            BudgetType type = oldMovement.getBudgetType();
            type.setTotalSpent(type.getTotalSpent() - oldTotalValue + newTotalValue);
            budgetTypeService.save(type);
        }
    }

    public static void removeMovementValueFromBudget(Movement movement, BudgetSubtypeService budgetSubtypeService,
                                                     BudgetTypeService budgetTypeService) {
        Double totalValue = movement.getTotalValue();

        if (movement.getBudgetSubtype() != null) {
            BudgetSubtype subtype = movement.getBudgetSubtype();
            subtype.setTotalSpent(subtype.getTotalSpent() - totalValue);
            budgetSubtypeService.save(subtype);
        } else if (movement.getBudgetType() != null) {
            BudgetType type = movement.getBudgetType();
            type.setTotalSpent(type.getTotalSpent() - totalValue);
            budgetTypeService.save(type);
        }
    }
}