package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.*;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.model.Movement;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.UUID;

public interface MovementService {
    MovementDTO create(MovementDTO movementDTO) throws BudgetSubtypeNotFoundException, SupplierNotFoundException, MovementAlreadyExistsException, MovementValidationException, InvoiceNotFoundException, BudgetExceededException, BudgetTypeNotFoundException;
    MovementDTO updateMovementStatus(MovementUpdateStatusRequestDTO movementUpdateStatusRequestDTO) throws MovementNotFoundException, MovementValidationException, BudgetExceededException, DocumentNumberNotFoundException;
    Movement getMovementEntityById(UUID id) throws MovementNotFoundException;
    Page<MovementDTO> getAll(CustomPageableDTO customPageableDTO) throws JsonProcessingException;
    MovementDTO getMovementDTOById(UUID id) throws MovementNotFoundException;
    MovementDTO update(MovementDTO movementDTO)
            throws MovementNotFoundException, SupplierNotFoundException, BudgetSubtypeNotFoundException, MovementAlreadyExistsException, MovementValidationException, InvoiceNotFoundException, BudgetExceededException, BudgetTypeNotFoundException;
    void delete(UUID id) throws MovementNotFoundException;
    Page<MovementDTO> getMovementsByBudgetType(MovementsByBudgetRequestDTO movementsByBudgetRequestDTO) throws Exception;
    Page<MovementDTO> getMovementsByBudgetSubtype(MovementsByBudgetRequestDTO movementsByBudgetRequestDTO) throws Exception;
    void exportMovements(ExportMovementsRequestDTO request) throws IOException, MovementNotFoundException, GenerateExcelException;
    Movement getMovementByDocumentNumber(String movementDocumentNumber) throws DocumentNumberNotFoundException;
}
