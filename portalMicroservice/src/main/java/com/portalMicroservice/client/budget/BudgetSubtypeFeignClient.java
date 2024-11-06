package com.portalMicroservice.client.budget;

import com.portalMicroservice.config.CustomErrorDecoder;
import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@FeignClient(name = "budgetSubtypeFeignClient", url = "${budget-microservice-budgetSubtype.url}", configuration = CustomErrorDecoder.class)
public interface BudgetSubtypeFeignClient {

    @PostMapping("/create")
    ResponseEntity<BudgetSubtypeDTO> addSubtype(@RequestBody BudgetSubtypeDTO budgetSubtypeDTO);

    @PutMapping("/update")
    ResponseEntity<BudgetSubtypeDTO> updateSubtype(@RequestBody BudgetSubtypeDTO budgetSubtypeDTO);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> deleteSubtype(@PathVariable("id") UUID id);

    @GetMapping("/{id}")
    ResponseEntity<BudgetSubtypeDTO> findSubtypeById(@PathVariable("id") UUID id);

    @GetMapping("/all")
    ResponseEntity<Page<BudgetSubtypeDTO>> findAllSubtypes(Pageable pageable);
}
