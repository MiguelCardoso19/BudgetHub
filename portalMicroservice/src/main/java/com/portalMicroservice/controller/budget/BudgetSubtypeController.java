package com.portalMicroservice.controller.budget;

import com.portalMicroservice.client.budget.BudgetSubtypeFeignClient;
import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/budget/subtype")
@RequiredArgsConstructor
public class BudgetSubtypeController {
    private final BudgetSubtypeFeignClient budgetSubtypeFeignClient;

    @PostMapping("/create")
    public ResponseEntity<BudgetSubtypeDTO> addSubtype(@RequestBody BudgetSubtypeDTO budgetSubtypeDTO){
        return budgetSubtypeFeignClient.addSubtype(budgetSubtypeDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<BudgetSubtypeDTO> updateSubtype(@RequestBody BudgetSubtypeDTO budgetSubtypeDTO) {
        return budgetSubtypeFeignClient.updateSubtype(budgetSubtypeDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSubtype(@PathVariable UUID id) {
        return budgetSubtypeFeignClient.deleteSubtype(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetSubtypeDTO> findSubtypeById(@PathVariable UUID id) {
        return budgetSubtypeFeignClient.findSubtypeById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<BudgetSubtypeDTO>> findAllSubtypes(Pageable pageable)  {
        return budgetSubtypeFeignClient.findAllSubtypes(pageable);
    }
}