package com.portalMicroservice.controller.budget;

import com.portalMicroservice.client.budget.BudgetTypeFeignClient;
import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.BudgetTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/budget/type")
@RequiredArgsConstructor
public class BudgetTypeController {
    private final BudgetTypeFeignClient budgetTypeFeignClient;
    private final BudgetTypeService budgetTypeService;

    @PostMapping("/create")
    public ResponseEntity<BudgetTypeDTO> createBudgetType(@RequestBody BudgetTypeDTO budgetTypeDTO) throws ExecutionException, InterruptedException, GenericException {
       // return budgetTypeFeignClient.createBudgetType(budgetTypeDTO);
        return ResponseEntity.ok(budgetTypeService.create(budgetTypeDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBudgetType(@PathVariable UUID id) {
       // return budgetTypeFeignClient.deleteBudgetType(id);
        budgetTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update")
    public ResponseEntity<BudgetTypeDTO> updateBudgetType(@Valid @RequestBody BudgetTypeDTO budgetTypeDTO) throws ExecutionException, InterruptedException, GenericException {
       // return budgetTypeFeignClient.updateBudgetType(budgetTypeDTO);
        return ResponseEntity.ok(budgetTypeService.update(budgetTypeDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetTypeDTO> findBudgetTypeById(@PathVariable UUID id) throws GenericException {
      //  return budgetTypeFeignClient.findBudgetTypeById(id);
        return ResponseEntity.ok(budgetTypeService.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<BudgetTypeDTO>> findAllBudgetTypes(Pageable pageable) {
        return budgetTypeFeignClient.findAllBudgetTypes(pageable);
    }
}
