package com.budgetMicroservice.validator;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.exception.MovementValidationException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MovementValidator {

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