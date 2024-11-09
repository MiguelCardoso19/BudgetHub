package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.*;
import com.budgetMicroservice.enumerator.MovementStatus;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.model.Movement;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

public interface MovementService {
    MovementDTO create(MovementDTO movementDTO) throws BudgetSubtypeNotFoundException, SupplierNotFoundException, MovementAlreadyExistsException, MovementValidationException, InvoiceNotFoundException, BudgetExceededException;
    MovementDTO updateMovementStatus(MovementUpdateStatusRequestDTO movementUpdateStatusRequestDTO) throws MovementNotFoundException, MovementValidationException, BudgetExceededException;
    Movement getMovementEntityById(UUID id) throws MovementNotFoundException;
    Page<MovementDTO> getAll(CustomPageableDTO customPageableDTO) throws JsonProcessingException;
    MovementDTO getMovementDTOById(UUID id) throws MovementNotFoundException;
    MovementDTO update(MovementDTO movementDTO)
            throws MovementNotFoundException, SupplierNotFoundException, BudgetSubtypeNotFoundException, MovementAlreadyExistsException, MovementValidationException, InvoiceNotFoundException, BudgetExceededException;
    void delete(UUID id) throws MovementNotFoundException;
    Page<MovementDTO> getMovementsByBudgetType(MovementsByBudgetRequestDTO movementsByBudgetRequestDTO) throws Exception;
    Page<MovementDTO> getMovementsByBudgetSubtype(MovementsByBudgetRequestDTO movementsByBudgetRequestDTO) throws Exception;
    void exportMovements(ExportMovementsRequestDTO request) throws IOException, MovementNotFoundException, GenerateExcelException;
    MovementStatus getMovementStatus(UUID id) throws MovementNotFoundException;
}
