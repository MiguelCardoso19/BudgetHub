package com.budgetMicroservice.controller;

import com.budgetMicroservice.dto.BudgetSubtypeDTO;
import com.budgetMicroservice.exception.BudgetSubtypeAlreadyExistsException;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.BudgetTypeNotFoundException;
import com.budgetMicroservice.service.BudgetSubtypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/budget/subtype")
@RequiredArgsConstructor
public class BudgetSubtypeController {
    private final BudgetSubtypeService budgetSubtypeService;

    @PostMapping("/create")
    public ResponseEntity<BudgetSubtypeDTO> addSubtype(@Valid @RequestBody BudgetSubtypeDTO budgetSubtypeDTO) throws BudgetTypeNotFoundException, BudgetSubtypeAlreadyExistsException, BudgetSubtypeNotFoundException {
        return ResponseEntity.ok(budgetSubtypeService.addSubtypeToBudget(budgetSubtypeDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<BudgetSubtypeDTO> updateSubtype(@Valid @RequestBody BudgetSubtypeDTO budgetSubtypeDTO) throws BudgetSubtypeNotFoundException, BudgetSubtypeAlreadyExistsException {
        return ResponseEntity.ok(budgetSubtypeService.updateBudgetSubtype(budgetSubtypeDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSubtype(@PathVariable UUID id) throws BudgetSubtypeNotFoundException {
        budgetSubtypeService.deleteBudgetSubtype(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetSubtypeDTO> findSubtypeById(@PathVariable UUID id) throws BudgetSubtypeNotFoundException {
        return ResponseEntity.ok(budgetSubtypeService.findBudgetSubtypeById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<BudgetSubtypeDTO>> findAllSubtypes(
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(budgetSubtypeService.findAllBudgetSubtypes(pageable));
    }
}