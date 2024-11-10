package com.portalMicroservice.controller.budget;

import com.portalMicroservice.client.budget.SupplierFeignClient;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.SupplierDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/supplier")
@RequiredArgsConstructor
@Tag(name = "Supplier Controller", description = "Operations related to suppliers management")
public class SupplierController {
    private final SupplierService supplierService;
    private final SupplierFeignClient supplierFeignClient;

    @Operation(summary = "Create a new supplier",
            description = "Creates a new supplier in the system with validated supplier details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the supplier"),
            @ApiResponse(responseCode = "400", description = "Bad request, validation failed"),
            @ApiResponse(responseCode = "409", description = "Supplier already exists")
    })
    @PostMapping("/create")
    public ResponseEntity<SupplierDTO> create(@RequestBody SupplierDTO supplierDTO)
            throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        return ResponseEntity.ok(supplierService.create(supplierDTO));
        // return supplierFeignClient.createSupplier(supplierDTO);
    }

    @Operation(summary = "Update an existing supplier",
            description = "Updates details of an existing supplier in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the supplier"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "400", description = "Bad request, validation failed")
    })
    @PutMapping("/update")
    public ResponseEntity<SupplierDTO> update(@RequestBody SupplierDTO supplierDTO)
            throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        return ResponseEntity.ok(supplierService.update(supplierDTO));
        // return supplierFeignClient.updateSupplier(supplierDTO);
    }

    @Operation(summary = "Delete a supplier",
            description = "Deletes an existing supplier by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the supplier"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        supplierService.delete(id);
        // supplierFeignClient.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get supplier by ID",
            description = "Fetches a specific supplier by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the supplier"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getById(@PathVariable UUID id)
            throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        return ResponseEntity.ok(supplierService.getById(id));
        // return supplierFeignClient.getSupplierById(id);
    }

    @Operation(summary = "Get all suppliers",
            description = "Fetches a paginated list of all suppliers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the list of suppliers"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping("/all")
    public ResponseEntity<CustomPageDTO> getAll(Pageable pageable)
            throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        // return supplierFeignClient.getAllSuppliers(pageable);
        return ResponseEntity.ok(supplierService.getAll(pageable));
    }
}
