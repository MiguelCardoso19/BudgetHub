package com.portalMicroservice.client.budget;

import com.portalMicroservice.config.CustomErrorDecoder;
import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "budgetTypeFeignClient", url = "${budget-microservice-budgetType.url}", configuration = CustomErrorDecoder.class)
public interface BudgetTypeFeignClient {

    @PostMapping("/create")
    ResponseEntity<BudgetTypeDTO> createBudgetType(@RequestBody BudgetTypeDTO budgetTypeDTO);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> deleteBudgetType(@PathVariable("id") UUID id);

    @PutMapping("/update")
    ResponseEntity<BudgetTypeDTO> updateBudgetType(@RequestBody BudgetTypeDTO budgetTypeDTO);

    @GetMapping("/{id}")
    ResponseEntity<BudgetTypeDTO> findBudgetTypeById(@PathVariable("id") UUID id);

    @GetMapping("/all")
    ResponseEntity<Page<BudgetTypeDTO>> findAllBudgetTypes(Pageable pageable);
}
