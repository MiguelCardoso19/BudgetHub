package com.portalMicroservice.controller.budget;

import com.portalMicroservice.client.budget.BudgetTypeFeignClient;
import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.BudgetTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/budget/type")
@RequiredArgsConstructor
@Tag(name = "Budget Type Controller", description = "Operations related to Budget Types")
public class BudgetTypeController {
    private final BudgetTypeFeignClient budgetTypeFeignClient;
    private final BudgetTypeService budgetTypeService;

    @Operation(summary = "Create a new Budget Type",
            description = "Creates a new Budget Type. This operation will check for existing budget types and throw an error if a duplicate is found.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a new Budget Type"),
            @ApiResponse(responseCode = "409", description = "Budget Type already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid Budget Type data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    public ResponseEntity<BudgetTypeDTO> createBudgetType(@RequestBody BudgetTypeDTO budgetTypeDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        // return budgetTypeFeignClient.createBudgetType(budgetTypeDTO);
        return ResponseEntity.ok(budgetTypeService.create(budgetTypeDTO));
    }

    @Operation(summary = "Delete a Budget Type",
            description = "Deletes a Budget Type by its unique ID. Throws a 404 error if the Budget Type is not found.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the Budget Type"),
            @ApiResponse(responseCode = "404", description = "Budget Type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBudgetType(@PathVariable UUID id) {
        // return budgetTypeFeignClient.deleteBudgetType(id);
        budgetTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update an existing Budget Type",
            description = "Updates an existing Budget Type. If the Budget Type does not exist or a conflict occurs, the operation will throw appropriate errors.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the Budget Type"),
            @ApiResponse(responseCode = "404", description = "Budget Type not found"),
            @ApiResponse(responseCode = "409", description = "Budget Type already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid Budget Type data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update")
    public ResponseEntity<BudgetTypeDTO> updateBudgetType(@Valid @RequestBody BudgetTypeDTO budgetTypeDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        // return budgetTypeFeignClient.updateBudgetType(budgetTypeDTO);
        return ResponseEntity.ok(budgetTypeService.update(budgetTypeDTO));
    }

    @Operation(summary = "Get a Budget Type by ID",
            description = "Fetches a specific Budget Type by its unique ID. Returns a 404 if the Budget Type is not found.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the Budget Type"),
            @ApiResponse(responseCode = "404", description = "Budget Type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BudgetTypeDTO> findBudgetTypeById(@PathVariable UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        //  return budgetTypeFeignClient.findBudgetTypeById(id);
        return ResponseEntity.ok(budgetTypeService.getById(id));
    }

    @Operation(summary = "Get all Budget Types",
            description = "Retrieves all Budget Types, supporting pagination. Returns a paginated list of Budget Types.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the list of Budget Types"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public ResponseEntity<CustomPageDTO> findAllBudgetTypes(Pageable pageable) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        // return budgetTypeFeignClient.findAllBudgetTypes(pageable);
        return ResponseEntity.ok(budgetTypeService.findAll(pageable));
    }
}