package com.portalMicroservice.controller.budget;

import com.portalMicroservice.client.budget.BudgetSubtypeFeignClient;
import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.BudgetSubtypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/budget/subtype")
@RequiredArgsConstructor
@Tag(name = "Budget Subtype Controller", description = "Operations related to budget subtypes")
public class BudgetSubtypeController {
    private final BudgetSubtypeService budgetSubtypeService;
    private final BudgetSubtypeFeignClient budgetSubtypeFeignClient;

    @Operation(summary = "Create a new budget subtype",
            description = "Creates a new budget subtype for a given budget type.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a new budget subtype"),
            @ApiResponse(responseCode = "404", description = "Budget type not found"),
            @ApiResponse(responseCode = "409", description = "Budget subtype already exists"),
            @ApiResponse(responseCode = "400", description = "Budget exceeded"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/create")
    public ResponseEntity<BudgetSubtypeDTO> addSubtype(@RequestBody BudgetSubtypeDTO budgetSubtypeDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        // return budgetSubtypeFeignClient.addSubtype(budgetSubtypeDTO);
        return ResponseEntity.ok(budgetSubtypeService.addSubtypeToBudget(budgetSubtypeDTO));
    }

    @Operation(summary = "Update an existing budget subtype",
            description = "Updates the details of an existing budget subtype.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the budget subtype"),
            @ApiResponse(responseCode = "404", description = "Budget subtype not found"),
            @ApiResponse(responseCode = "409", description = "Budget subtype already exists"),
            @ApiResponse(responseCode = "400", description = "Budget exceeded"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update")
    public ResponseEntity<BudgetSubtypeDTO> updateSubtype(@RequestBody BudgetSubtypeDTO budgetSubtypeDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        // return budgetSubtypeFeignClient.updateSubtype(budgetSubtypeDTO);
        return ResponseEntity.ok(budgetSubtypeService.update(budgetSubtypeDTO));
    }

    @Operation(summary = "Delete a budget subtype",
            description = "Deletes a budget subtype by its ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the budget subtype"),
            @ApiResponse(responseCode = "404", description = "Budget subtype not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSubtype(@PathVariable UUID id) throws ExecutionException, InterruptedException, TimeoutException {
        // budgetSubtypeFeignClient.deleteSubtype(id);
        budgetSubtypeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a budget subtype by ID",
            description = "Fetches a budget subtype by its ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the budget subtype"),
            @ApiResponse(responseCode = "404", description = "Budget subtype not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<BudgetSubtypeDTO> findSubtypeById(@PathVariable UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        // return budgetSubtypeFeignClient.findSubtypeById(id);
        return ResponseEntity.ok(budgetSubtypeService.getById(id));
    }

    @Operation(summary = "Get all budget subtypes",
            description = "Fetches all budget subtypes, paginated.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the list of budget subtypes"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<CustomPageDTO> findAllSubtypes(Pageable pageable) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        // return budgetSubtypeFeignClient.findAllSubtypes(pageable);
        return ResponseEntity.ok(budgetSubtypeService.findAll(pageable));
    }
}
