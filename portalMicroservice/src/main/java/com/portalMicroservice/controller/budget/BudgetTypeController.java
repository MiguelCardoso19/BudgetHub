package com.portalMicroservice.controller.budget;

import com.portalMicroservice.client.budget.BudgetTypeFeignClient;
import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/budget/type")
@RequiredArgsConstructor
public class BudgetTypeController {
    private final BudgetTypeFeignClient budgetTypeFeignClient;

    @PostMapping("/create")
    public ResponseEntity<BudgetTypeDTO> createBudgetType(@RequestBody BudgetTypeDTO budgetTypeDTO){
        return budgetTypeFeignClient.createBudgetType(budgetTypeDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBudgetType(@PathVariable UUID id) {
        return budgetTypeFeignClient.deleteBudgetType(id);
    }

    @PutMapping("/update")
    public ResponseEntity<BudgetTypeDTO> updateBudgetType(@Valid @RequestBody BudgetTypeDTO budgetTypeDTO) {
        return budgetTypeFeignClient.updateBudgetType(budgetTypeDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetTypeDTO> findBudgetTypeById(@PathVariable UUID id){
        return budgetTypeFeignClient.findBudgetTypeById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<BudgetTypeDTO>> findAllBudgetTypes(Pageable pageable) {
        return budgetTypeFeignClient.findAllBudgetTypes(pageable);
    }
}
