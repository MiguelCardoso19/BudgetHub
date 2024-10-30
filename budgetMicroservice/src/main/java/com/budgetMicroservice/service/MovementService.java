package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.MovementAlreadyExistsException;
import com.budgetMicroservice.exception.MovementNotFoundException;
import com.budgetMicroservice.exception.SupplierNotFoundException;
import com.budgetMicroservice.model.Movement;

import java.util.UUID;

public interface MovementService {
    MovementDTO createMovement(MovementDTO movementDTO) throws BudgetSubtypeNotFoundException, SupplierNotFoundException, MovementAlreadyExistsException;
    MovementDTO updateMovementStatus(UUID id) throws MovementNotFoundException;
    Movement findById(UUID id) throws MovementNotFoundException;
}
