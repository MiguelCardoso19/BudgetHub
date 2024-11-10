package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.MovementDTO;
import com.portalMicroservice.exception.GenericException;

public interface MovementEventListenerService {
    void handleMovementResponse(MovementDTO movementDTO) throws GenericException;
    void handleMovementPageResponse(CustomPageDTO customPageDTO) throws GenericException;
}
