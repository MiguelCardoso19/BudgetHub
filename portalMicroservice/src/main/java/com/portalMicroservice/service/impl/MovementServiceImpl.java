package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.*;
import com.portalMicroservice.enumerator.MovementStatus;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.MovementService;
import com.portalMicroservice.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovementServiceImpl implements MovementService {
    private final ConcurrentHashMap<UUID, CompletableFuture<MovementDTO>> pendingRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, CompletableFuture<CustomPageDTO>> pendingPageRequests = new ConcurrentHashMap<>();
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, MovementDTO> kafkaMovementTemplate;
    private final KafkaTemplate<String, CustomPageableDTO> kafkaPageableTemplate;
    private final KafkaTemplate<String, MovementsByBudgetRequestDTO> kafkaMovementsByBudgetTemplate;
    private final KafkaTemplate<String, ExportMovementsRequestDTO> kafkaExportMovementsTemplate;

    private static final long TIMEOUT_DURATION = 10;

    @Override
    public MovementDTO create(MovementDTO movementDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(movementDTO.getId(), future);
        kafkaMovementTemplate.send("create-movement", movementDTO);
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }

    @Override
    public MovementDTO update(MovementDTO movementDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(movementDTO.getId(), future);
        kafkaMovementTemplate.send("update-movement", movementDTO);
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
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
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }

    @Override
    public CustomPageDTO getAll(Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(pageable.getPageSize(), future);
        kafkaPageableTemplate.send("get-all-movements", PageableUtils.convertToCustomPageable(pageable));
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }

    @Override
    public CustomPageDTO getByBudgetType(UUID budgetTypeId, Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(pageable.getPageSize(), future);
        kafkaMovementsByBudgetTemplate.send("get-movements-by-budget-type",
                new MovementsByBudgetRequestDTO(budgetTypeId, PageableUtils.convertToCustomPageable(pageable)));
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }

    @Override
    public CustomPageDTO getByBudgetSubtype(UUID budgetSubtypeId, Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(pageable.getPageSize(), future);
        kafkaMovementsByBudgetTemplate.send("get-movements-by-budget-subtype",
                new MovementsByBudgetRequestDTO(budgetSubtypeId, PageableUtils.convertToCustomPageable(pageable)));
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }

    @Override
    public void exportMovementsReport(LocalDate startDate, LocalDate endDate, MovementStatus status, String emailFromRequest) {
        kafkaExportMovementsTemplate.send("export-movements-report",
                new ExportMovementsRequestDTO(startDate, endDate, status, emailFromRequest));
    }

    @KafkaListener(topics = "movement-response", groupId = "movement_response_group", concurrency = "10", containerFactory = "movementKafkaListenerContainerFactory")
    public void listen(MovementDTO movementDTO) throws GenericException {
        CompletableFuture<MovementDTO> future = pendingRequests.remove(movementDTO.getId());

        if (future != null) {
            future.complete(movementDTO);
        } else {
            throw new GenericException();
        }
    }

    @KafkaListener(topics = "movement-page-response", groupId = "pageable_response_group", concurrency = "10", containerFactory = "customPageKafkaListenerContainerFactory")
    public void listenToPageResponse(CustomPageDTO customPageDTO) throws GenericException {
        CompletableFuture<CustomPageDTO> future = pendingPageRequests.remove(customPageDTO.getPageable().getPageSize());

        if (future != null) {
            future.complete(customPageDTO);
        } else {
            throw new GenericException();
        }
    }
}
