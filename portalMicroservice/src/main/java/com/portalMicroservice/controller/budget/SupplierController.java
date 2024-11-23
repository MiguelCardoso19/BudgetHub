package com.portalMicroservice.controller.budget;

import com.portalMicroservice.client.budget.SupplierFeignClient;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.SupplierDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.budget.SupplierNotFoundException;
import com.portalMicroservice.service.SupplierService;
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
@RequestMapping("/api/v1/supplier")
@RequiredArgsConstructor
@Tag(name = "Supplier Controller", description = "Operations related to suppliers management")
public class SupplierController {
    private final SupplierService supplierService;
    private final SupplierFeignClient supplierFeignClient;

    @Operation(summary = "Create a new supplier",
            description = "Creates a new supplier in the system with validated supplier details.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the supplier"),
            @ApiResponse(responseCode = "400", description = "Bad request, validation failed"),
            @ApiResponse(responseCode = "409", description = "Supplier already exists")
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/create")
    public ResponseEntity<SupplierDTO> create(@RequestBody SupplierDTO supplierDTO)
            throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        // return supplierFeignClient.createSupplier(supplierDTO);
        return ResponseEntity.ok(supplierService.create(supplierDTO));
    }

    @Operation(summary = "Update an existing supplier",
            description = "Updates details of an existing supplier in the system.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the supplier"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "400", description = "Bad request, validation failed")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update")
    public ResponseEntity<SupplierDTO> update(@RequestBody SupplierDTO supplierDTO)
            throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        // return supplierFeignClient.updateSupplier(supplierDTO);
        return ResponseEntity.ok(supplierService.update(supplierDTO));
    }

    @Operation(summary = "Delete a supplier",
            description = "Deletes an existing supplier by its ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the supplier"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws ExecutionException, InterruptedException, TimeoutException {
        supplierService.delete(id);
        // supplierFeignClient.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get supplier by ID",
            description = "Fetches a specific supplier by its ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the supplier"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getById(@PathVariable UUID id)
            throws GenericException, ExecutionException, InterruptedException, TimeoutException, SupplierNotFoundException {
        // return supplierFeignClient.getSupplierById(id);
        return ResponseEntity.ok(supplierService.getById(id));
    }

    @Operation(summary = "Get all suppliers",
            description = "Fetches a paginated list of all suppliers.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the list of suppliers"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<CustomPageDTO> getAll(Pageable pageable)
            throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        // return supplierFeignClient.getAllSuppliers(pageable);
        return ResponseEntity.ok(supplierService.getAll(pageable));
    }
}
