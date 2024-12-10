package com.portalMicroservice.client.budget;

import com.portalMicroservice.config.CustomErrorDecoder;
import com.portalMicroservice.dto.budget.SupplierDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@FeignClient(name = "supplierFeignClient", url = "${budget-microservice-supplier.url}", configuration = CustomErrorDecoder.class)
public interface SupplierFeignClient {

    @PostMapping("/create")
    ResponseEntity<SupplierDTO> createSupplier(@RequestBody SupplierDTO supplierDTO);

    @GetMapping("/{id}")
    ResponseEntity<SupplierDTO> getSupplierById(@PathVariable UUID id);

    @PutMapping("/update")
    ResponseEntity<SupplierDTO> updateSupplier(@RequestBody SupplierDTO supplierDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSupplier(@PathVariable UUID id);

    @GetMapping("/all")
    ResponseEntity<Page<SupplierDTO>> getAllSuppliers(@RequestParam Pageable pageable);
}
