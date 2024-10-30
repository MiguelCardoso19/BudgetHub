package com.budgetMicroservice.validator;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.exception.MovementAlreadyExistsException;
import com.budgetMicroservice.repository.MovementRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MovementValidator {

    public static void validateDocumentNumberForExistingMovement(MovementDTO movementDTO,
                                                                 MovementRepository repository)
            throws MovementAlreadyExistsException {
        log.info("Checking for existing document number: {}", movementDTO.getDocumentNumber());

        if (repository.existsByDocumentNumber(movementDTO.getDocumentNumber())) {
            log.error("Validation failed: Document number already exists. Document number: {}", movementDTO.getDocumentNumber());
            throw new MovementAlreadyExistsException(movementDTO.getDocumentNumber());
        }
    }

    public static void validateDocumentNumberForExistingMovementUpdate(MovementDTO movementDTO,
                                                                       MovementRepository repository)
            throws MovementAlreadyExistsException {
        log.info("Checking for existing document number (update): {}", movementDTO.getDocumentNumber());

        if (repository.existsByDocumentNumberAndIdNot(movementDTO.getDocumentNumber(), movementDTO.getId())) {
            log.error("Validation failed: Document number already exists. Document number: {}", movementDTO.getDocumentNumber());
            throw new MovementAlreadyExistsException(movementDTO.getDocumentNumber());
        }
    }
}