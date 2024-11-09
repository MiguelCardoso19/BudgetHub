package com.portalMicroservice.controller.budget;

import com.portalMicroservice.client.budget.MovementFeignClient;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.MovementDTO;
import com.portalMicroservice.enumerator.MovementStatus;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.MovementService;
import com.portalMicroservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/movement")
@RequiredArgsConstructor
@Slf4j
public class MovementController {
    private final MovementService movementService;
    private final MovementFeignClient movementFeignClient;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<MovementDTO> create(@RequestBody MovementDTO movementDTO) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        // return movementFeignClient.createMovement(movementDTO);
        return ResponseEntity.ok(movementService.create(movementDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<MovementDTO> update(@RequestBody MovementDTO movementDTO) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        // return movementFeignClient.updateMovement(movementDTO);
        return ResponseEntity.ok(movementService.update(movementDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovementDTO> getById(@PathVariable UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        // return movementFeignClient.getMovementById(id);
        return ResponseEntity.ok(movementService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        // movementFeignClient.deleteMovement(id);
        movementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<CustomPageDTO> getAll(Pageable pageable) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
      //  return movementFeignClient.getAllMovements(pageable);
        return ResponseEntity.ok(movementService.getAll(pageable));
    }

    @GetMapping("/budget-type/{budgetTypeId}")
    public ResponseEntity<CustomPageDTO> getByBudgetType(@PathVariable UUID budgetTypeId, Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        //return movementFeignClient.getMovementsByBudgetType(budgetTypeId, pageable);
        return ResponseEntity.ok(movementService.getByBudgetType(budgetTypeId, pageable));
    }

    @GetMapping("/budget-subtype/{budgetSubtypeId}")
    public ResponseEntity<CustomPageDTO> getByBudgetSubtype(@PathVariable UUID budgetSubtypeId, Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
       // return movementFeignClient.getMovementsByBudgetSubtype(budgetSubtypeId, pageable);
         return ResponseEntity.ok(movementService.getByBudgetSubtype(budgetSubtypeId, pageable));
    }

    @PostMapping("/export-movements-report")
    public ResponseEntity<Void> exportMovementsReport(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) MovementStatus status
    ) {
        // movementFeignClient.exportMovementsReport(startDate, endDate, status, jwtService.getEmailFromRequest());
         movementService.exportMovementsReport(startDate, endDate, status, jwtService.getEmailFromRequest());
       return ResponseEntity.ok().build();
    }
}
