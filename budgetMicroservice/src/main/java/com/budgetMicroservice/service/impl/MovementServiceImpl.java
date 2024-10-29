//package com.budgetMicroservice.service.impl;
//
//import com.budgetMicroservice.dto.MovementDTO;
//import com.budgetMicroservice.model.BudgetSubtype;
//import com.budgetMicroservice.model.BudgetType;
//import com.budgetMicroservice.model.Movement;
//import com.budgetMicroservice.repository.BudgetSubtypeRepository;
//import com.budgetMicroservice.repository.BudgetTypeRepository;
//import com.budgetMicroservice.repository.MovementRepository;
//import com.budgetMicroservice.repository.SupplierRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class MovementServiceImpl {
//    private MovementRepository movementRepository;
//    private SupplierRepository supplierRepository;
//    private BudgetTypeRepository budgetTypeRepository;
//    private BudgetSubtypeRepository budgetSubtypeRepository;
//
//    public Movement createMovement(MovementDTO dto) {
//        Movement movement = new Movement();
//        movement.setSupplier(supplierRepository.findById(dto.getSupplierId()).orElseThrow());
//        movement.setType(budgetTypeRepository.findById(dto.getTypeId()).orElseThrow());
//        movement.setSubtype(budgetSubtypeRepository.findById(dto.getSubtypeId()).orElseThrow());
//        movement.setMovementType(dto.getMovementType());
//        movement.setDateOfEmission(dto.getDateOfEmission());
//        movement.setDocumentNumber(dto.getDocumentNumber());
//        movement.setDescription(dto.getDescription());
//        movement.setValueWithoutIva(dto.getValueWithoutIva());
//        movement.setIvaValue(dto.getIvaValue());
//        movement.setTotalValueWithIva(dto.getValueWithoutIva().add(dto.getIvaValue()));
//        movement.setPaid(false);
//
//        Movement savedMovement = movementRepository.save(movement);
//        updateSpentAmounts(savedMovement.getType(), savedMovement.getSubtype(), savedMovement.getTotalValueWithIva());
//        return savedMovement;
//    }
//
//    public Movement updateMovementStatus(UUID movementId, boolean paidStatus) {
//        Movement movement = movementRepository.findById(movementId).orElseThrow(() -> new RuntimeException("Movement not found"));
//        movement.setPaid(paidStatus);
//
//        return movementRepository.save(movement);
//    }
//
//    private void updateSpentAmounts(BudgetType type, BudgetSubtype subtype, Double amount) {
//        subtype.setTotalSpent(subtype.getTotalSpent().add(amount));
//        budgetSubtypeRepository.save(subtype);
//
//        type.setTotalSpent(type.getTotalSpent().add(amount));
//        budgetTypeRepository.save(type);
//    }
//}
