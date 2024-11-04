package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.enumerator.MovementStatus;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.model.Movement;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

public interface MovementService {
    MovementDTO create(MovementDTO movementDTO) throws BudgetSubtypeNotFoundException, SupplierNotFoundException, MovementAlreadyExistsException, MovementValidationException, InvoiceNotFoundException;
    MovementDTO updateMovementStatus(UUID id, MovementStatus status) throws MovementNotFoundException;
    Movement getMovementEntityById(UUID id) throws MovementNotFoundException;
    Page<MovementDTO> getAll(Pageable pageable) throws JsonProcessingException;
    MovementDTO getMovementDTOById(UUID id) throws MovementNotFoundException;
    MovementDTO update(MovementDTO movementDTO)
            throws MovementNotFoundException, SupplierNotFoundException, BudgetSubtypeNotFoundException, MovementAlreadyExistsException, MovementValidationException, InvoiceNotFoundException;
    void delete(UUID id) throws MovementNotFoundException;
    Page<MovementDTO> getMovementsByBudgetType(UUID budgetTypeId, Pageable pageable) throws MovementsNotFoundForBudgetTypeException, JsonProcessingException;
    Page<MovementDTO> getMovementsByBudgetSubtype(UUID budgetSubtypeId, Pageable pageable) throws MovementsNotFoundForBudgetSubtypeException, JsonProcessingException;
    void exportAndSendMovements(LocalDate startDate, LocalDate endDate, MovementStatus status, String userEmail) throws IOException, MovementNotFoundException, GenerateExcelException;
    MovementStatus getMovementStatus(UUID id) throws MovementNotFoundException;
}
