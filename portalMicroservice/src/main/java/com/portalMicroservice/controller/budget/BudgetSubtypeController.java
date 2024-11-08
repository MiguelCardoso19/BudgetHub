package com.portalMicroservice.controller.budget;

import com.portalMicroservice.client.budget.BudgetSubtypeFeignClient;
import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.BudgetSubtypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/budget/subtype")
@RequiredArgsConstructor
public class BudgetSubtypeController {
    private final BudgetSubtypeService budgetSubtypeService;
    private final BudgetSubtypeFeignClient budgetSubtypeFeignClient;

    @PostMapping("/create")
    public ResponseEntity<BudgetSubtypeDTO> addSubtype(@RequestBody BudgetSubtypeDTO budgetSubtypeDTO) throws ExecutionException, InterruptedException, GenericException {
        // return budgetSubtypeFeignClient.addSubtype(budgetSubtypeDTO);
        return ResponseEntity.ok(budgetSubtypeService.addSubtypeToBudget(budgetSubtypeDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<BudgetSubtypeDTO> updateSubtype(@RequestBody BudgetSubtypeDTO budgetSubtypeDTO) throws ExecutionException, InterruptedException, GenericException {
        // return budgetSubtypeFeignClient.updateSubtype(budgetSubtypeDTO);
        return ResponseEntity.ok(budgetSubtypeService.update(budgetSubtypeDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSubtype(@PathVariable UUID id) {
        // return budgetSubtypeFeignClient.deleteSubtype(id);
        budgetSubtypeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetSubtypeDTO> findSubtypeById(@PathVariable UUID id) throws GenericException {
        // return budgetSubtypeFeignClient.findSubtypeById(id);
        return ResponseEntity.ok(budgetSubtypeService.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<BudgetSubtypeDTO>> findAllSubtypes(Pageable pageable) {
         return budgetSubtypeFeignClient.findAllSubtypes(pageable);
      //  return ResponseEntity.ok(budgetSubtypeService.findAll(pageable));
    }
}
