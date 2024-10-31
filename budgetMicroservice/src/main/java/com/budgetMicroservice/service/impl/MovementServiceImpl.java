package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.mapper.MovementMapper;
import com.budgetMicroservice.model.*;
import com.budgetMicroservice.repository.MovementRepository;
import com.budgetMicroservice.service.*;
import com.budgetMicroservice.util.MovementUtils; // Import the new utility class
import com.budgetMicroservice.validator.MovementValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public MovementDTO create(MovementDTO movementDTO) throws BudgetSubtypeNotFoundException, SupplierNotFoundException, MovementAlreadyExistsException, MovementValidationException {
        Supplier supplier = supplierService.findSupplierEntityById(movementDTO.getSupplierId());

        MovementValidator.validateDocumentNumberForExistingMovement(movementDTO, movementRepository);
        MovementUtils.calculateIvaAndTotal(movementDTO);

        Movement movement = movementMapper.toEntity(movementDTO);
        movement.setSupplier(supplier);

        MovementUtils.setBudget(movement, movementDTO, budgetSubtypeService, budgetTypeService);

        if (movement.isPaid()) {
            MovementUtils.updateSpentAmounts(budgetSubtypeService, budgetTypeService, movement, movement.getTotalValue());
        }

        Movement savedMovement = movementRepository.save(movement);
        return movementMapper.toDTO(savedMovement);
    }

    @Override
    @Transactional
    public MovementDTO update(MovementDTO movementDTO) throws MovementNotFoundException, SupplierNotFoundException, BudgetSubtypeNotFoundException, MovementAlreadyExistsException, MovementValidationException {
        Movement existingMovement = movementRepository.findById(movementDTO.getId()).orElseThrow(() -> new MovementNotFoundException(movementDTO.getId()));

        Supplier existingSupplier = supplierService.findSupplierEntityById(movementDTO.getSupplierId());

        MovementValidator.validateDocumentNumberForExistingMovementUpdate(movementDTO, movementRepository);
        MovementUtils.calculateIvaAndTotal(movementDTO);

        if (existingMovement.isPaid()) {
            MovementUtils.adjustBudgetAmounts(budgetSubtypeService, budgetTypeService, existingMovement, movementDTO);
        }

        movementMapper.updateFromDTO(movementDTO, existingMovement);
        existingMovement.setSupplier(existingSupplier);
        MovementUtils.setBudget(existingMovement, movementDTO, budgetSubtypeService, budgetTypeService);

        return movementMapper.toDTO(movementRepository.save(existingMovement));
    }

    @Override
    public Movement getMovementEntityById(UUID id) throws MovementNotFoundException {
        return findById(id);
    }

    @Override
    public MovementDTO getMovementDTOById(UUID id) throws MovementNotFoundException {
        return movementMapper.toDTO(findById(id));
    }

    @Override
    public void delete(UUID id) throws MovementNotFoundException {
        Movement existingMovement = movementRepository.findById(id).orElseThrow(() -> new MovementNotFoundException(id));

        MovementUtils.removeMovementValueFromBudget(existingMovement, budgetSubtypeService, budgetTypeService);

        movementRepository.deleteById(id);
    }


    @Override
    public Page<MovementDTO> getAll(Pageable pageable) {
        return movementRepository.findAll(pageable).map(movementMapper::toDTO);
    }

    @Override
    public Page<MovementDTO> getMovementsByBudgetType(UUID budgetTypeId, Pageable pageable) throws MovementsNotFoundForBudgetTypeException {
        Page<Movement> movements = movementRepository.findByBudgetTypeId(budgetTypeId, pageable);
        if (movements.isEmpty()) {
            throw new MovementsNotFoundForBudgetTypeException(budgetTypeId);
        }

        return movements.map(movementMapper::toDTOWithoutBudgetType);
    }

    @Override
    public Page<MovementDTO> getMovementsByBudgetSubtype(UUID budgetSubtypeId, Pageable pageable) throws MovementsNotFoundForBudgetSubtypeException {
        Page<Movement> movements = movementRepository.findByBudgetSubtypeId(budgetSubtypeId, pageable);

        if (movements.isEmpty()) {
            throw new MovementsNotFoundForBudgetSubtypeException(budgetSubtypeId);
        }

        return movements.map(movementMapper::toDTOWithoutBudgetSubtype);
    }

    @Override
    public MovementDTO updateMovementStatus(UUID id) throws MovementNotFoundException {
        Movement movement = movementRepository.findById(id).orElseThrow(() -> new MovementNotFoundException(id));
        movement.setPaid(true);
        MovementUtils.updateSpentAmounts(budgetSubtypeService, budgetTypeService, movement, movement.getTotalValue());
        return movementMapper.toDTO(movementRepository.save(movement));
    }

    private Movement findById(UUID id) throws MovementNotFoundException {
        return movementRepository.findById(id).orElseThrow(() -> new MovementNotFoundException(id));
    }
}