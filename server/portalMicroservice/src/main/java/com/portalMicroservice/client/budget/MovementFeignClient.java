package com.portalMicroservice.client.budget;

import com.portalMicroservice.config.CustomErrorDecoder;
import com.portalMicroservice.dto.budget.MovementDTO;
import com.portalMicroservice.enumerator.MovementStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

@FeignClient(name = "movementFeignClient", url = "${budget-microservice-movement.url}", configuration = CustomErrorDecoder.class)
public interface MovementFeignClient {

    @PostMapping("/create")
    ResponseEntity<MovementDTO> createMovement(@RequestBody MovementDTO movementDTO);

    @PutMapping("/update")
    ResponseEntity<MovementDTO> updateMovement(@RequestBody MovementDTO movementDTO);

    @GetMapping("/{id}")
    ResponseEntity<MovementDTO> getMovementById(@PathVariable UUID id);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteMovement(@PathVariable UUID id);

    @GetMapping("/all")
    ResponseEntity<Page<MovementDTO>> getAllMovements(@RequestParam Pageable pageable);

    @GetMapping("/budget-type/{budget-type-id}")
    ResponseEntity<Page<MovementDTO>> getMovementsByBudgetType(@PathVariable("budget-type-id") UUID budgetTypeId,
                                                               @RequestParam Pageable pageable);

    @GetMapping("/budget-subtype/{budget-subtype-id}")
    ResponseEntity<Page<MovementDTO>> getMovementsByBudgetSubtype(@PathVariable("budget-subtype-id") UUID budgetSubtypeId,
                                                                  @RequestParam Pageable pageable);

    @PostMapping("/export-movements-report")
    ResponseEntity<Void> exportMovementsReport(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) MovementStatus status,
            @RequestParam String userEmail);

}
