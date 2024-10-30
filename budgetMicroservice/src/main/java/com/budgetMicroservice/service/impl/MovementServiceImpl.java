package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.MovementAlreadyExistsException;
import com.budgetMicroservice.exception.MovementNotFoundException;
import com.budgetMicroservice.exception.SupplierNotFoundException;
import com.budgetMicroservice.mapper.MovementMapper;
import com.budgetMicroservice.model.*;
import com.budgetMicroservice.repository.MovementRepository;
import com.budgetMicroservice.service.*;
import com.budgetMicroservice.validator.MovementValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {
    private final BudgetSubtypeService budgetSubtypeService;
    private final BudgetTypeService budgetTypeService;
    private final SupplierService supplierService;
    private final MovementMapper movementMapper;
    private final MovementRepository movementRepository;

    @Override
    @Transactional
    public MovementDTO createMovement(MovementDTO movementDTO)
            throws BudgetSubtypeNotFoundException, SupplierNotFoundException, MovementAlreadyExistsException {
        MovementValidator.validateDocumentNumberForExistingMovement(movementDTO, movementRepository);

        Double ivaValue = (movementDTO.getValueWithoutIva() * movementDTO.getIvaRate()) / 100;
        Double totalValue = movementDTO.getValueWithoutIva() + ivaValue;

        movementDTO.setIvaValue(ivaValue);
        movementDTO.setTotalValue(totalValue);

        Movement movement = movementMapper.toEntity(movementDTO);
        movement.setSupplier(supplierService.findById(movementDTO.getSupplierId()));

        if (movementDTO.getSubtypeId() != null) {
            BudgetSubtype budgetSubtype = budgetSubtypeService.findById(movementDTO.getSubtypeId());
            movement.setBudgetSubtype(budgetSubtype);
            updateSpentAmounts(budgetSubtype, null, totalValue);
        } else if (movementDTO.getTypeId() != null) {
            BudgetType budgetType = budgetTypeService.findById(movementDTO.getTypeId());
            movement.setBudgetType(budgetType);
            updateSpentAmounts(null, budgetType, totalValue);
        }

        Movement savedMovement = movementRepository.save(movement);
        return movementMapper.toDTO(savedMovement);
    }

    @Override
    public MovementDTO updateMovementStatus(UUID id) throws MovementNotFoundException {
        Movement movement = movementRepository.findById(id).orElseThrow(() -> new MovementNotFoundException(id));
        movement.setPaid(true);

        return movementMapper.toDTO(movementRepository.save(movement));
    }

    @Override
    public Movement findById(UUID id) throws MovementNotFoundException {
        return movementRepository.findById(id).orElseThrow(() -> new MovementNotFoundException(id));
    }

    /**
     *
     *  FIX: CALL THEIR SERVICES, ONLY UPDATE AFTER PAID STATUS = TRUE
     *
     */
    private void updateSpentAmounts(BudgetSubtype subtype, BudgetType type, Double amount) {
        if (subtype != null) {
            subtype.setTotalSpent(subtype.getTotalSpent() + amount);
            budgetSubtypeService.save(subtype);
        }

        if (type != null) {
            type.setTotalSpent(type.getTotalSpent() + amount);
            budgetTypeService.save(type);
        }
    }
}
