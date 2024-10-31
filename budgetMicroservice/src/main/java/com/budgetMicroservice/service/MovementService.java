package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.model.Movement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MovementService {
    MovementDTO create(MovementDTO movementDTO) throws BudgetSubtypeNotFoundException, SupplierNotFoundException, MovementAlreadyExistsException, MovementValidationException;
    MovementDTO updateMovementStatus(UUID id) throws MovementNotFoundException;
    Movement getMovementEntityById(UUID id) throws MovementNotFoundException;
    Page<MovementDTO> getAll(Pageable pageable);
    MovementDTO getMovementDTOById(UUID id) throws MovementNotFoundException;
    MovementDTO update(MovementDTO movementDTO)
            throws MovementNotFoundException, SupplierNotFoundException, BudgetSubtypeNotFoundException, MovementAlreadyExistsException, MovementValidationException;
    void delete(UUID id) throws MovementNotFoundException;
    Page<MovementDTO> getMovementsByBudgetType(UUID budgetTypeId, Pageable pageable) throws MovementsNotFoundForBudgetTypeException;
    Page<MovementDTO> getMovementsByBudgetSubtype(UUID budgetSubtypeId, Pageable pageable) throws MovementsNotFoundForBudgetSubtypeException;
}
