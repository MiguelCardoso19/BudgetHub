package com.portalMicroservice.controller.budget;

import com.portalMicroservice.client.budget.MovementFeignClient;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.MovementDTO;
import com.portalMicroservice.enumerator.MovementStatus;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.MovementService;
import com.portalMicroservice.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/movement")
@RequiredArgsConstructor
@Tag(name = "Movement Controller", description = "Operations related to financial movements")
public class MovementController {
    private final MovementService movementService;
    private final MovementFeignClient movementFeignClient;
    private final JwtService jwtService;

    @Operation(summary = "Create a new movement",
            description = "Creates a new financial movement.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movement successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    public ResponseEntity<MovementDTO> create(@RequestBody MovementDTO movementDTO) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        // return movementFeignClient.createMovement(movementDTO);
        return ResponseEntity.ok(movementService.create(movementDTO));
    }

    @Operation(summary = "Update an existing movement",
            description = "Updates an existing movement by providing updated details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movement successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid update data"),
            @ApiResponse(responseCode = "404", description = "Movement not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update")
    public ResponseEntity<MovementDTO> update(@RequestBody MovementDTO movementDTO) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        // return movementFeignClient.updateMovement(movementDTO);
        return ResponseEntity.ok(movementService.update(movementDTO));
    }

    @Operation(summary = "Get a movement by ID",
            description = "Fetches a movement by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movement found and returned"),
            @ApiResponse(responseCode = "404", description = "Movement not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovementDTO> getById(@PathVariable UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        // return movementFeignClient.getMovementById(id);
        return ResponseEntity.ok(movementService.getById(id));
    }

    @Operation(summary = "Delete a movement by ID",
            description = "Deletes an existing movement based on the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Movement successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Movement not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        // movementFeignClient.deleteMovement(id);
        movementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Fetch all movements with pagination",
            description = "Returns a paginated list of all movements.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of movements retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping("/all")
    public ResponseEntity<CustomPageDTO> getAll(Pageable pageable) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        // return movementFeignClient.getAllMovements(pageable);
        return ResponseEntity.ok(movementService.getAll(pageable));
    }

    @Operation(summary = "Fetch movements by budget type",
            description = "Retrieves movements filtered by budget type ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movements filtered by budget type ID retrieved"),
            @ApiResponse(responseCode = "404", description = "Budget type not found"),
            @ApiResponse(responseCode = "400", description = "Invalid budget type ID or pagination parameters")
    })
    @GetMapping("/budget-type/{budgetTypeId}")
    public ResponseEntity<CustomPageDTO> getByBudgetType(@PathVariable UUID budgetTypeId, Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        // return movementFeignClient.getMovementsByBudgetType(budgetTypeId, pageable);
        return ResponseEntity.ok(movementService.getByBudgetType(budgetTypeId, pageable));
    }

    @Operation(summary = "Fetch movements by budget subtype",
            description = "Retrieves movements filtered by budget subtype ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movements filtered by budget subtype ID retrieved"),
            @ApiResponse(responseCode = "404", description = "Budget subtype not found"),
            @ApiResponse(responseCode = "400", description = "Invalid budget subtype ID or pagination parameters")
    })
    @GetMapping("/budget-subtype/{budgetSubtypeId}")
    public ResponseEntity<CustomPageDTO> getByBudgetSubtype(@PathVariable UUID budgetSubtypeId, Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        // return movementFeignClient.getMovementsByBudgetSubtype(budgetSubtypeId, pageable);
        return ResponseEntity.ok(movementService.getByBudgetSubtype(budgetSubtypeId, pageable));
    }

    @Operation(summary = "Export movements report",
            description = "Exports a report of movements within a date range and status, sent to the user's email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report export initiated"),
            @ApiResponse(responseCode = "400", description = "Invalid date range or status parameters"),
            @ApiResponse(responseCode = "500", description = "Error generating or exporting the report")
    })
    @PostMapping("/export-movements-report")
    public ResponseEntity<Void> exportMovementsReport(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) MovementStatus status
    ) {
        // movementFeignClient.exportMovementsReport(startDate, endDate, status, jwtService.getEmailFromRequest());
        movementService.exportMovementsReport(startDate, endDate, status, jwtService.getEmailFromRequest());
        return ResponseEntity.ok().build();
    }
}
