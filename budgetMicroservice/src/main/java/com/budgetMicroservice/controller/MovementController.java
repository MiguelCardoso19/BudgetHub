package com.budgetMicroservice.controller;

import com.budgetMicroservice.dto.ExportMovementsRequestDTO;
import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.dto.MovementUpdateStatusRequestDTO;
import com.budgetMicroservice.dto.MovementsByBudgetRequestDTO;
import com.budgetMicroservice.enumerator.MovementStatus;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.service.MovementService;
import com.budgetMicroservice.util.PageableUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movement")
@RequiredArgsConstructor
public class MovementController {
    private final MovementService movementService;

    @PostMapping("/create")
    public ResponseEntity<MovementDTO> createMovement(@Valid @RequestBody MovementDTO movementDTO)
            throws BudgetSubtypeNotFoundException, SupplierNotFoundException, MovementAlreadyExistsException, MovementValidationException, InvoiceNotFoundException, BudgetExceededException {
        return ResponseEntity.ok(movementService.create(movementDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<MovementDTO> updateMovement(@Valid @RequestBody MovementDTO movementDTO)
            throws MovementNotFoundException, SupplierNotFoundException, BudgetSubtypeNotFoundException, MovementAlreadyExistsException, MovementValidationException, InvoiceNotFoundException, BudgetExceededException {
        return ResponseEntity.ok(movementService.update(movementDTO));
    }

    @PutMapping("/status/update/{id}")
    public ResponseEntity<MovementDTO> updateMovementStatus(@RequestBody MovementUpdateStatusRequestDTO movementUpdateStatusRequestDTO) throws MovementNotFoundException, MovementValidationException, BudgetExceededException {
        return ResponseEntity.ok(movementService.updateMovementStatus(movementUpdateStatusRequestDTO));
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
    public ResponseEntity<Page<MovementDTO>> getAllMovements(@PageableDefault(size = 10, page = 0) Pageable pageable) throws JsonProcessingException {
        return ResponseEntity.ok(movementService.getAll(PageableUtils.convertToCustomPageable(pageable)));
    }

    @GetMapping("/budget-type/{budget-type-id}")
    public ResponseEntity<Page<MovementDTO>> getMovementsByBudgetType(@PathVariable("budget-type-id") UUID budgetTypeId,
                                                                      @PageableDefault(size = 10, page = 0) Pageable pageable)
            throws Exception {
        return ResponseEntity.ok(movementService.getMovementsByBudgetType(
                new MovementsByBudgetRequestDTO(budgetTypeId, PageableUtils.convertToCustomPageable(pageable))));
    }

    @GetMapping("/budget-subtype/{budget-subtype-id}")
    public ResponseEntity<Page<MovementDTO>> getMovementsByBudgetSubtype(@PathVariable("budget-subtype-id") UUID budgetSubtypeId,
                                                                         @PageableDefault(size = 10, page = 0)Pageable pageable)
            throws Exception {
        return ResponseEntity.ok(movementService.getMovementsByBudgetSubtype(
                new MovementsByBudgetRequestDTO(budgetSubtypeId, PageableUtils.convertToCustomPageable(pageable))));
    }

    @PostMapping("/export-movements-report")
    public ResponseEntity<Void> exportMovementsReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) MovementStatus status,
            @RequestParam("userEmail") String userEmail) throws IOException, MovementNotFoundException, GenerateExcelException {
            movementService.exportMovements(new ExportMovementsRequestDTO(startDate,endDate, status, userEmail));
            return ResponseEntity.ok().build();
    }
}
