package com.portalMicroservice.controller.budget;

import com.portalMicroservice.client.budget.MovementFeignClient;
import com.portalMicroservice.dto.budget.MovementDTO;
import com.portalMicroservice.enumerator.MovementStatus;
import com.portalMicroservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movement")
@RequiredArgsConstructor
@Slf4j
public class MovementController {
    private final MovementFeignClient movementFeignClient;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<MovementDTO> create(@RequestBody MovementDTO movementDTO) {
        return movementFeignClient.createMovement(movementDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<MovementDTO> update(@RequestBody MovementDTO movementDTO) {
        return movementFeignClient.updateMovement(movementDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovementDTO> getById(@PathVariable UUID id) {
        return movementFeignClient.getMovementById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        movementFeignClient.deleteMovement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<MovementDTO>> getAll(Pageable pageable) {
        return movementFeignClient.getAllMovements(pageable);
    }

    @GetMapping("/budget-type/{budgetTypeId}")
    public ResponseEntity<Page<MovementDTO>> getByBudgetType(@PathVariable UUID budgetTypeId, Pageable pageable) {
        return movementFeignClient.getMovementsByBudgetType(budgetTypeId, pageable);
    }

    @GetMapping("/budget-subtype/{budgetSubtypeId}")
    public ResponseEntity<Page<MovementDTO>> getByBudgetSubtype(@PathVariable UUID budgetSubtypeId, Pageable pageable) {
        return movementFeignClient.getMovementsByBudgetSubtype(budgetSubtypeId, pageable);
    }

    @PostMapping("/export-movements-report")
    public ResponseEntity<Void> exportMovementsReport(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) MovementStatus status
    ) {
        return movementFeignClient.exportMovementsReport(startDate, endDate, status, jwtService.getEmailFromRequest());
    }
}
