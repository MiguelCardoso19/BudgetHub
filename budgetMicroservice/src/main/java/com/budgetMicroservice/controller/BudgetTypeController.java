package com.budgetMicroservice.controller;

import com.budgetMicroservice.dto.BudgetTypeDTO;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.BudgetTypeAlreadyExistsException;
import com.budgetMicroservice.exception.BudgetTypeNotFoundException;
import com.budgetMicroservice.service.BudgetTypeService;
import com.budgetMicroservice.util.PageableUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/budget/type")
@RequiredArgsConstructor
public class BudgetTypeController {
    private final BudgetTypeService budgetTypeService;

    @PostMapping("/create")
    public ResponseEntity<BudgetTypeDTO> createBudgetType(@Valid @RequestBody BudgetTypeDTO budgetTypeDTO) throws BudgetTypeAlreadyExistsException {
        return ResponseEntity.ok(budgetTypeService.createBudgetType(budgetTypeDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBudgetType(@PathVariable UUID id) throws BudgetTypeNotFoundException {
        budgetTypeService.deleteBudgetType(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update")
    public ResponseEntity<BudgetTypeDTO> updateBudgetType(@Valid @RequestBody BudgetTypeDTO budgetTypeDTO) throws BudgetTypeNotFoundException, BudgetTypeAlreadyExistsException, BudgetSubtypeNotFoundException {
        return ResponseEntity.ok(budgetTypeService.updateBudgetType(budgetTypeDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetTypeDTO> findBudgetTypeById(@PathVariable UUID id) throws BudgetTypeNotFoundException, BudgetSubtypeNotFoundException {
        return ResponseEntity.ok(budgetTypeService.findBudgetTypeDTOById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<BudgetTypeDTO>> findAllBudgetTypes(
            @PageableDefault(size = 10, page = 0) Pageable pageable) throws JsonProcessingException {
        return ResponseEntity.ok(budgetTypeService.findAllBudgetTypes(PageableUtils.convertToCustomPageable(pageable)));
    }
}