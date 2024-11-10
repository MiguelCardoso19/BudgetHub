package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.*;
import com.portalMicroservice.enumerator.MovementStatus;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.MovementService;
import com.portalMicroservice.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovementServiceImpl implements MovementService {
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, MovementDTO> kafkaMovementTemplate;
    private final KafkaTemplate<String, CustomPageableDTO> kafkaPageableTemplate;
    private final KafkaTemplate<String, MovementsByBudgetRequestDTO> kafkaMovementsByBudgetTemplate;
    private final KafkaTemplate<String, ExportMovementsRequestDTO> kafkaExportMovementsTemplate;

    private final ConcurrentHashMap<UUID, CompletableFuture<MovementDTO>> pendingRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, CompletableFuture<CustomPageDTO>> pendingPageRequests = new ConcurrentHashMap<>();

    @Value("${kafka-timeout-duration}")
    private long TIMEOUT_DURATION;

    @Override
    public MovementDTO create(MovementDTO movementDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        movementDTO.setCorrelationId(UUID.randomUUID());
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(movementDTO.getCorrelationId(), future);
        kafkaMovementTemplate.send("create-movement", movementDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public MovementDTO update(MovementDTO movementDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(movementDTO.getId(), future);
        kafkaMovementTemplate.send("update-movement", movementDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public void delete(UUID id) {
        kafkaUuidTemplate.send("delete-movement", id);
    }

    @Override
    public MovementDTO getById(UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        kafkaUuidTemplate.send("get-movement-by-id", id);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public CustomPageDTO getAll(Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        CustomPageableDTO customPageableDTO = PageableUtils.convertToCustomPageable(pageable);
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);
        kafkaPageableTemplate.send("get-all-movements", customPageableDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public CustomPageDTO getByBudgetType(UUID budgetTypeId, Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        CustomPageableDTO customPageableDTO = PageableUtils.convertToCustomPageable(pageable);
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);
        kafkaMovementsByBudgetTemplate.send("get-movements-by-budget-type", new MovementsByBudgetRequestDTO(budgetTypeId, customPageableDTO));
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public CustomPageDTO getByBudgetSubtype(UUID budgetSubtypeId, Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        CustomPageableDTO customPageableDTO = PageableUtils.convertToCustomPageable(pageable);
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);
        log.info("sent {}", customPageableDTO.getCorrelationId());
        kafkaMovementsByBudgetTemplate.send("get-movements-by-budget-subtype", new MovementsByBudgetRequestDTO(budgetSubtypeId, customPageableDTO));
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public void exportMovementsReport(LocalDate startDate, LocalDate endDate, MovementStatus status, String emailFromRequest) {
        kafkaExportMovementsTemplate.send("export-movements-report",
                new ExportMovementsRequestDTO(startDate, endDate, status, emailFromRequest));
    }

    public CompletableFuture<MovementDTO> getPendingRequest(UUID correlationId, UUID id) {
        return pendingRequests.remove(correlationId != null ? correlationId : id);
    }

    public CompletableFuture<CustomPageDTO> getPendingPageRequest(UUID correlationId) {
        return pendingPageRequests.remove(correlationId);
    }
}
