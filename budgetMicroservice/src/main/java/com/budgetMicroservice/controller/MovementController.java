package com.budgetMicroservice.controller;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.MovementAlreadyExistsException;
import com.budgetMicroservice.exception.MovementNotFoundException;
import com.budgetMicroservice.exception.SupplierNotFoundException;
import com.budgetMicroservice.service.MovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movement")
@RequiredArgsConstructor
public class MovementController {
    private final MovementService movementService;

    @PostMapping("/create")
    public ResponseEntity<MovementDTO> createMovement(@Valid @RequestBody MovementDTO dto) throws BudgetSubtypeNotFoundException, SupplierNotFoundException, MovementAlreadyExistsException {
        return ResponseEntity.ok(movementService.createMovement(dto));
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<MovementDTO> updateMovementStatus(@PathVariable UUID id) throws MovementNotFoundException {
        return ResponseEntity.ok(movementService.updateMovementStatus(id));
    }
}
