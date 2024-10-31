package com.budgetMicroservice.controller;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.service.MovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movement")
@RequiredArgsConstructor
public class MovementController {
    private final MovementService movementService;

    @PostMapping("/create")
    public ResponseEntity<MovementDTO> createMovement(@Valid @RequestBody MovementDTO movementDTO)
            throws BudgetSubtypeNotFoundException, SupplierNotFoundException, MovementAlreadyExistsException, MovementValidationException {
        return ResponseEntity.ok(movementService.create(movementDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<MovementDTO> updateMovement(@Valid @RequestBody MovementDTO movementDTO)
            throws MovementNotFoundException, SupplierNotFoundException, BudgetSubtypeNotFoundException, MovementAlreadyExistsException, MovementValidationException {
        return ResponseEntity.ok(movementService.update(movementDTO));
    }

    @PutMapping("/status/update/{id}")
    public ResponseEntity<MovementDTO> updateMovementStatus(@PathVariable UUID id) throws MovementNotFoundException {
        return ResponseEntity.ok(movementService.updateMovementStatus(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovementDTO> getMovementById(@PathVariable UUID id) throws MovementNotFoundException {
        return ResponseEntity.ok(movementService.getMovementDTOById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovement(@PathVariable UUID id) throws MovementNotFoundException {
        movementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<MovementDTO>> getAllMovements(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(movementService.getAll(pageable));
    }

    @GetMapping("/budget-type/{budget-type-id}")
    public ResponseEntity<Page<MovementDTO>> getMovementsByBudgetType(@PathVariable("budget-type-id") UUID budgetTypeId,
                                                                      @PageableDefault(size = 10, page = 0) Pageable pageable)
            throws MovementsNotFoundForBudgetTypeException {
        return ResponseEntity.ok(movementService.getMovementsByBudgetType(budgetTypeId, pageable));
    }

    @GetMapping("/budget-subtype/{budget-subtype-id}")
    public ResponseEntity<Page<MovementDTO>> getMovementsByBudgetSubtype(@PathVariable("budget-subtype-id") UUID budgetSubtypeId,
                                                                         @PageableDefault(size = 10, page = 0)Pageable pageable)
            throws MovementsNotFoundForBudgetSubtypeException {
        return ResponseEntity.ok(movementService.getMovementsByBudgetSubtype(budgetSubtypeId, pageable));
    }

}
