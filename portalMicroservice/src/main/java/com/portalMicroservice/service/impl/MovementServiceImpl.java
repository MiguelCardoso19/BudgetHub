package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.MovementDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.MovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovementServiceImpl implements MovementService {
    private final ConcurrentHashMap<UUID, CompletableFuture<MovementDTO>> pendingRequests = new ConcurrentHashMap<>();
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, MovementDTO> kafkaMovementTemplate;

    private static final long TIMEOUT_DURATION = 10;

    @Override
    public MovementDTO create(MovementDTO movementDTO) throws ExecutionException, InterruptedException, GenericException {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(movementDTO.getId(), future);

        kafkaMovementTemplate.send("create-movement", movementDTO);

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("Request timed out while creating movement: {}", movementDTO.getId());
            throw new GenericException();
        }
    }

    @Override
    public MovementDTO update(MovementDTO movementDTO) throws ExecutionException, InterruptedException, GenericException {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(movementDTO.getId(), future);

        kafkaMovementTemplate.send("update-movement", movementDTO);

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("Request timed out while updating movement: {}", movementDTO.getId());
            throw new GenericException();
        }
    }

    @Override
    public void delete(UUID id) {
        kafkaUuidTemplate.send("delete-movement", id);
    }

    @Override
    public MovementDTO getById(UUID id) throws GenericException {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);

        kafkaUuidTemplate.send("get-movement-by-id", id);

        log.info("Sent request to retrieve movement by ID: {}", id);

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            log.error("Request timed out while retrieving movement by ID: {}", id);
            throw new GenericException();
        }
    }

    @KafkaListener(topics = "movement-response", groupId = "movement_response_group", concurrency = "10", containerFactory = "movementKafkaListenerContainerFactory")
    public void listen(MovementDTO movementDTO) throws GenericException {
        log.info("Received message for Movement: {}", movementDTO.getId());

        CompletableFuture<MovementDTO> future = pendingRequests.remove(movementDTO.getId());

        if (future != null) {
            future.complete(movementDTO);
        } else {
            log.warn("Received response for unknown or expired request ID: {}", movementDTO.getId());
            throw new GenericException();
        }
    }
}
