package com.budgetMicroservice.validator;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.exception.MovementAlreadyExistsException;
import com.budgetMicroservice.exception.MovementValidationException;
import com.budgetMicroservice.repository.MovementRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

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

    public static void validateMovementValues(MovementDTO movementDTO) throws MovementValidationException {
        List<String> errorMessages = new ArrayList<>();

        if (movementDTO.getValueWithoutIva() <= 0) {
            errorMessages.add("Value without IVA must be greater than 0");
        }

        if (movementDTO.getIvaRate() < 0) {
            errorMessages.add("IVA rate must be greater than or equal to 0 if provided");
        }

        if (!errorMessages.isEmpty()) {
            log.error("Movement validation failed with errors: {}", errorMessages);
            throw new MovementValidationException(errorMessages);
        }
    }
}