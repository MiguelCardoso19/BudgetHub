package com.budgetMicroservice.controller;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.dto.MovementUpdateStatusRequestDTO;
import com.budgetMicroservice.dto.ExportMovementsRequestDTO;
import com.budgetMicroservice.dto.MovementsByBudgetRequestDTO;
import com.budgetMicroservice.enumerator.MovementStatus;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.service.MovementService;
import com.budgetMicroservice.util.PageableUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movement")
@RequiredArgsConstructor
@Tag(name = "Movement Controller", description = "Operations related to financial movements")
public class MovementController {
    private final MovementService movementService;

    @Operation(summary = "Create a new movement",
            description = "Creates a new financial movement. The movement should be validated with all relevant details, including supplier, invoice, and budget subtype.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the movement"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data"),
            @ApiResponse(responseCode = "409", description = "Movement already exists"),
            @ApiResponse(responseCode = "404", description = "Supplier or Invoice not found"),
            @ApiResponse(responseCode = "406", description = "Budget exceeded")
    })
    @PostMapping("/create")
    public ResponseEntity<MovementDTO> createMovement(@Valid @RequestBody MovementDTO movementDTO)
            throws BudgetSubtypeNotFoundException, SupplierNotFoundException, MovementAlreadyExistsException, MovementValidationException, InvoiceNotFoundException, BudgetExceededException, BudgetTypeNotFoundException {
        return ResponseEntity.ok(movementService.create(movementDTO));
    }

    @Operation(summary = "Update an existing movement",
            description = "Updates an existing financial movement. The movement must exist, and all the related entities (supplier, invoice, budget) must be validated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the movement"),
            @ApiResponse(responseCode = "404", description = "Movement not found"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data")
    })
    @PutMapping("/update")
    public ResponseEntity<MovementDTO> updateMovement(@Valid @RequestBody MovementDTO movementDTO)
            throws MovementNotFoundException, SupplierNotFoundException, BudgetSubtypeNotFoundException, MovementAlreadyExistsException, MovementValidationException, InvoiceNotFoundException, BudgetExceededException, BudgetTypeNotFoundException {
        return ResponseEntity.ok(movementService.update(movementDTO));
    }

    @Operation(summary = "Update the status of a movement",
            description = "Updates the status of a movement (e.g., Canceled, Pending). Only valid statuses are allowed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the status"),
            @ApiResponse(responseCode = "404", description = "Movement not found"),
            @ApiResponse(responseCode = "400", description = "Invalid status or bad request")
    })
    @PutMapping("/status/update")
    public ResponseEntity<MovementDTO> updateMovementStatus(@RequestBody MovementUpdateStatusRequestDTO movementUpdateStatusRequestDTO)
            throws MovementNotFoundException, MovementValidationException, BudgetExceededException, DocumentNumberNotFoundException {
        return ResponseEntity.ok(movementService.updateMovementStatus(movementUpdateStatusRequestDTO));
    }

    @Operation(summary = "Get a movement by ID",
            description = "Fetches a specific movement by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the movement"),
            @ApiResponse(responseCode = "404", description = "Movement not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovementDTO> getMovementById(@PathVariable UUID id)
            throws MovementNotFoundException {
        return ResponseEntity.ok(movementService.getMovementDTOById(id));
    }

    @Operation(summary = "Delete a movement",
            description = "Deletes an existing movement by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the movement"),
            @ApiResponse(responseCode = "404", description = "Movement not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovement(@PathVariable UUID id) throws MovementNotFoundException {
        movementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all movements",
            description = "Fetches all movements with pagination support.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the list of movements"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping("/all")
    public ResponseEntity<Page<MovementDTO>> getAllMovements(@PageableDefault(size = 10, page = 0) Pageable pageable)
            throws JsonProcessingException {
        return ResponseEntity.ok(movementService.getAll(PageableUtils.convertToCustomPageable(pageable)));
    }

    @Operation(summary = "Get movements by budget type",
            description = "Fetches movements filtered by budget type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched movements by budget type"),
            @ApiResponse(responseCode = "400", description = "Invalid budget type or pagination parameters")
    })
    @GetMapping("/budget-type/{budget-type-id}")
    public ResponseEntity<Page<MovementDTO>> getMovementsByBudgetType(
            @PathVariable("budget-type-id") UUID budgetTypeId,
            @PageableDefault(size = 10, page = 0) Pageable pageable)
            throws Exception {
        return ResponseEntity.ok(movementService.getMovementsByBudgetType(
                new MovementsByBudgetRequestDTO(null, budgetTypeId, PageableUtils.convertToCustomPageable(pageable))));
    }

    @Operation(summary = "Get movements by budget subtype",
            description = "Fetches movements filtered by budget subtype.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched movements by budget subtype"),
            @ApiResponse(responseCode = "400", description = "Invalid budget subtype or pagination parameters")
    })
    @GetMapping("/budget-subtype/{budget-subtype-id}")
    public ResponseEntity<Page<MovementDTO>> getMovementsByBudgetSubtype(
            @PathVariable("budget-subtype-id") UUID budgetSubtypeId,
            @PageableDefault(size = 10, page = 0) Pageable pageable)
            throws Exception {
        return ResponseEntity.ok(movementService.getMovementsByBudgetSubtype(
                new MovementsByBudgetRequestDTO(null, budgetSubtypeId, PageableUtils.convertToCustomPageable(pageable))));
    }

    @Operation(summary = "Export movements report",
            description = "Exports the movements report as an Excel file, filtered by date range and status. The report will be sent to the user email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully exported the report"),
            @ApiResponse(responseCode = "400", description = "Invalid date range or status parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/export-movements-report")
    public ResponseEntity<Void> exportMovementsReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) MovementStatus status,
            @RequestParam("userEmail") String userEmail)
            throws IOException, MovementNotFoundException, GenerateExcelException {
        movementService.exportMovements(new ExportMovementsRequestDTO(null, startDate, endDate, status, userEmail));
        return ResponseEntity.ok().build();
    }
}