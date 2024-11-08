package com.portalMicroservice.controller.budget;

import com.portalMicroservice.client.budget.SupplierFeignClient;
import com.portalMicroservice.dto.budget.SupplierDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/supplier")
@RequiredArgsConstructor
@Slf4j
public class SupplierController {
    private final SupplierService supplierService;
    private final SupplierFeignClient supplierFeignClient;

    @PostMapping("/create")
    public ResponseEntity<SupplierDTO> create(@RequestBody SupplierDTO supplierDTO) throws ExecutionException, InterruptedException, GenericException {
         return ResponseEntity.ok(supplierService.create(supplierDTO));
       // return supplierFeignClient.createSupplier(supplierDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<SupplierDTO> update(@RequestBody SupplierDTO supplierDTO) throws ExecutionException, InterruptedException, GenericException {
          return ResponseEntity.ok(supplierService.update(supplierDTO));
       // return supplierFeignClient.updateSupplier(supplierDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        supplierService.delete(id);
     //   supplierFeignClient.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getById(@PathVariable UUID id) throws GenericException, ExecutionException, InterruptedException {
         return ResponseEntity.ok(supplierService.getById(id));
       // return supplierFeignClient.getSupplierById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<SupplierDTO>> getAll(Pageable pageable) {
        return supplierFeignClient.getAllSuppliers(pageable);
    }
}