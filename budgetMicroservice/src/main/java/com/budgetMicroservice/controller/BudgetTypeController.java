package com.budgetMicroservice.controller;

import com.budgetMicroservice.dto.BudgetTypeDTO;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.service.BudgetTypeService;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/budget/type")
@RequiredArgsConstructor
@Tag(name = "Budget Type Controller", description = "Operations related to Budget Types")
public class BudgetTypeController {
    private final BudgetTypeService budgetTypeService;

    @Operation(
            summary = "Create a new Budget Type",
            description = "Creates a new Budget Type. This operation will check for existing budget types and throw an error if a duplicate is found."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a new Budget Type"),
            @ApiResponse(responseCode = "409", description = "Budget Type already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid Budget Type data")
    })
    @PostMapping("/create")
    public ResponseEntity<BudgetTypeDTO> createBudgetType(@Valid @RequestBody BudgetTypeDTO budgetTypeDTO) throws BudgetTypeAlreadyExistsException {
        return ResponseEntity.ok(budgetTypeService.createBudgetType(budgetTypeDTO));
    }

    @Operation(
            summary = "Delete a Budget Type",
            description = "Deletes a Budget Type by its unique ID. Throws a 404 error if the Budget Type is not found."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the Budget Type"),
            @ApiResponse(responseCode = "404", description = "Budget Type not found")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBudgetType(@PathVariable UUID id) throws BudgetTypeNotFoundException {
        budgetTypeService.deleteBudgetType(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update an existing Budget Type",
            description = "Updates an existing Budget Type. If the Budget Type does not exist or a conflict occurs, the operation will throw appropriate errors."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the Budget Type"),
            @ApiResponse(responseCode = "404", description = "Budget Type not found"),
            @ApiResponse(responseCode = "409", description = "Budget Type already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid Budget Type data")
    })
    @PutMapping("/update")
    public ResponseEntity<BudgetTypeDTO> updateBudgetType(@Valid @RequestBody BudgetTypeDTO budgetTypeDTO)
            throws BudgetTypeNotFoundException, BudgetTypeAlreadyExistsException, BudgetSubtypeNotFoundException {
        return ResponseEntity.ok(budgetTypeService.updateBudgetType(budgetTypeDTO));
    }

    @Operation(
            summary = "Get a Budget Type by ID",
            description = "Fetches a specific Budget Type by its unique ID. Returns a 404 if the Budget Type is not found."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the Budget Type"),
            @ApiResponse(responseCode = "404", description = "Budget Type not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BudgetTypeDTO> findBudgetTypeById(@PathVariable UUID id) throws BudgetTypeNotFoundException, BudgetSubtypeNotFoundException {
        return ResponseEntity.ok(budgetTypeService.findBudgetTypeDTOById(id));
    }

    @Operation(
            summary = "Get all Budget Types",
            description = "Retrieves all Budget Types, supporting pagination. Returns a paginated list of Budget Types."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the list of Budget Types"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public ResponseEntity<Page<BudgetTypeDTO>> findAllBudgetTypes(
            @PageableDefault(size = 10, page = 0) Pageable pageable) throws JsonProcessingException {
        return ResponseEntity.ok(budgetTypeService.findAllBudgetTypes(PageableUtils.convertToCustomPageable(pageable)));
    }
}
