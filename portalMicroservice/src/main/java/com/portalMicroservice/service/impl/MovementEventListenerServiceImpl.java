package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.MovementDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.MovementEventListenerService;
import com.portalMicroservice.service.MovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MovementEventListenerServiceImpl implements MovementEventListenerService {
    private final MovementService movementService;

    @KafkaListener(topics = "movement-response", groupId = "movement_response_group", concurrency = "10", containerFactory = "movementKafkaListenerContainerFactory")
    public void handleMovementResponse(MovementDTO movementDTO) throws GenericException {
        CompletableFuture<MovementDTO> future = movementService.getPendingRequest(movementDTO.getCorrelationId(), movementDTO.getId());

        if (future != null) {
            future.complete(movementDTO);
        } else {
            throw new GenericException();
        }
    }

    @KafkaListener(topics = "movement-page-response", groupId = "pageable_response_group", concurrency = "10", containerFactory = "customPageKafkaListenerContainerFactory")
    public void handleMovementPageResponse(CustomPageDTO customPageDTO) throws GenericException {
        CompletableFuture<CustomPageDTO> future = movementService.getPendingPageRequest(customPageDTO.getPageable().getCorrelationId());

        if (future != null) {
            future.complete(customPageDTO);
        } else {
            throw new GenericException();
        }
    }
}
