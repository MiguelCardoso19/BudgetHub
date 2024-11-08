package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.MovementDTO;
import com.portalMicroservice.exception.GenericException;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface MovementService {
    MovementDTO create(MovementDTO movementDTO) throws ExecutionException, InterruptedException, GenericException;
    MovementDTO update(MovementDTO movementDTO) throws ExecutionException, InterruptedException, GenericException;
    void delete(UUID id);
    MovementDTO getById(UUID id) throws GenericException;
}
