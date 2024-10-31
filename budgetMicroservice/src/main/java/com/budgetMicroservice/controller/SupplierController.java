package com.budgetMicroservice.controller;

import com.budgetMicroservice.dto.SupplierDTO;
import com.budgetMicroservice.exception.SupplierNotFoundException;
import com.budgetMicroservice.exception.SupplierValidationException;
import com.budgetMicroservice.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/supplier")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @PostMapping("/create")
    public ResponseEntity<SupplierDTO> create(@Valid @RequestBody SupplierDTO supplierDTO) throws SupplierValidationException {
        return ResponseEntity.ok(supplierService.create(supplierDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> findBySupplierById(@PathVariable UUID id) throws SupplierNotFoundException {
        return ResponseEntity.ok(supplierService.findSupplierDTOById(id));
    }

    @PutMapping("/update")
    public ResponseEntity<SupplierDTO> update(@Valid @RequestBody SupplierDTO supplierDTO)
            throws SupplierNotFoundException, SupplierValidationException {
        return ResponseEntity.ok(supplierService.update(supplierDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws SupplierNotFoundException {
        supplierService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<SupplierDTO>> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(supplierService.findAll(pageable));
    }
}