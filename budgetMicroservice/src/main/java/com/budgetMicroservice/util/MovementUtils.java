package com.budgetMicroservice.util;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.enumerator.MovementStatus;
import com.budgetMicroservice.exception.BudgetExceededException;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.MovementValidationException;
import com.budgetMicroservice.model.BudgetSubtype;
import com.budgetMicroservice.model.BudgetType;
import com.budgetMicroservice.model.Movement;
import com.budgetMicroservice.repository.MovementRepository;
import com.budgetMicroservice.service.BudgetSubtypeService;
import com.budgetMicroservice.service.BudgetTypeService;
import com.budgetMicroservice.validator.MovementValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class MovementUtils {

    public static void calculateIvaAndTotal(MovementDTO movementDTO) throws MovementValidationException {
        MovementValidator.validateMovementValues(movementDTO);

        Double ivaValue = (movementDTO.getValueWithoutIva() * (movementDTO.getIvaRate() != null ? movementDTO.getIvaRate() : 0)) / 100;
        Double totalValue = movementDTO.getValueWithoutIva() + ivaValue;

        movementDTO.setIvaValue(ivaValue);
        movementDTO.setTotalValue(totalValue);
    }

    public static void setBudget(Movement movement, MovementDTO movementDTO,
                                 BudgetSubtypeService budgetSubtypeService,
                                 BudgetTypeService budgetTypeService) throws BudgetSubtypeNotFoundException {
        if (movementDTO.getBudgetSubtypeId() != null) {
            BudgetSubtype budgetSubtype = budgetSubtypeService.findBudgetSubtypeEntityById(movementDTO.getBudgetSubtypeId());
            movement.setBudgetSubtype(budgetSubtype);
            movement.setBudgetType(null);
        } else if (movementDTO.getBudgetTypeId() != null) {
            BudgetType budgetType = budgetTypeService.findBudgetTypeEntityById(movementDTO.getBudgetTypeId());
            movement.setBudgetType(budgetType);
            movement.setBudgetSubtype(null);
        }
    }

    public static void updateSpentAmounts(BudgetSubtypeService budgetSubtypeService,
                                          BudgetTypeService budgetTypeService,
                                          Movement movement, Double totalValue) throws BudgetExceededException {

        if (movement.getBudgetSubtype() != null) {
            BudgetSubtype subtype = movement.getBudgetSubtype();
            BudgetType type = subtype.getBudgetType();

            if (totalValue > subtype.getAvailableFunds()) {
                throw new BudgetExceededException(totalValue, subtype.getAvailableFunds());
            }

            subtype.setAvailableFunds(subtype.getAvailableFunds() - totalValue);
            budgetSubtypeService.save(subtype);

            if (type != null) {
                type.setAvailableFunds(type.getAvailableFunds() - totalValue);
                budgetTypeService.save(type);
            }

        } else if (movement.getBudgetType() != null) {
            BudgetType type = movement.getBudgetType();

            if (totalValue > type.getAvailableFunds()) {
                throw new BudgetExceededException(totalValue, type.getAvailableFunds());
            }

            type.setAvailableFunds(type.getAvailableFunds() - totalValue);
            budgetTypeService.save(type);
        }
    }

    public static void adjustBudgetAmounts(BudgetSubtypeService budgetSubtypeService,
                                           BudgetTypeService budgetTypeService,
                                           Movement oldMovement, MovementDTO newMovementDTO) throws BudgetExceededException {
        double oldTotalValue = oldMovement.getTotalValue();
        double newTotalValue = newMovementDTO.getTotalValue();
        double valueDifference = newTotalValue - oldTotalValue;

        if (oldMovement.getBudgetSubtype() != null) {
            BudgetSubtype subtype = oldMovement.getBudgetSubtype();
            BudgetType type = subtype.getBudgetType();

            if (valueDifference > subtype.getAvailableFunds()) {
                throw new BudgetExceededException(valueDifference, subtype.getAvailableFunds());
            }

            subtype.setAvailableFunds(subtype.getAvailableFunds() - oldTotalValue + newTotalValue);
            budgetSubtypeService.save(subtype);

            if (type != null) {
                type.setAvailableFunds(type.getAvailableFunds() - oldTotalValue + newTotalValue);
                budgetTypeService.save(type);
            }
        } else if (oldMovement.getBudgetType() != null) {
            BudgetType type = oldMovement.getBudgetType();

            if (valueDifference > type.getAvailableFunds()) {
                throw new BudgetExceededException(valueDifference, type.getAvailableFunds());
            }

            type.setAvailableFunds(type.getAvailableFunds() - oldTotalValue + newTotalValue);
            budgetTypeService.save(type);
        }
    }

    public static void removeMovementValueFromBudget(Movement movement, BudgetSubtypeService budgetSubtypeService,
                                                     BudgetTypeService budgetTypeService) {
        Double totalValue = movement.getTotalValue();

        if (movement.getBudgetSubtype() != null) {
            BudgetSubtype subtype = movement.getBudgetSubtype();

            subtype.setAvailableFunds(subtype.getAvailableFunds() + totalValue);
            budgetSubtypeService.save(subtype);

            if (subtype.getBudgetType() != null) {
                BudgetType type = subtype.getBudgetType();
                type.setAvailableFunds(type.getAvailableFunds() + totalValue);
                budgetTypeService.save(type);
            }
        } else if (movement.getBudgetType() != null) {
            BudgetType type = movement.getBudgetType();

            type.setAvailableFunds(type.getAvailableFunds() + totalValue);
            budgetTypeService.save(type);
        }
    }


    public static void populateSheetWithMovements(XSSFSheet sheet, List<Movement> movements) {
        int rowIdx = 0;

        Row headerRow = sheet.createRow(rowIdx++);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Movement Type");
        headerRow.createCell(2).setCellValue("Date of Emission");
        headerRow.createCell(3).setCellValue("Description");
        headerRow.createCell(4).setCellValue("Value Without IVA");
        headerRow.createCell(5).setCellValue("IVA Value");
        headerRow.createCell(6).setCellValue("IVA Rate");
        headerRow.createCell(7).setCellValue("Total Value");
        headerRow.createCell(8).setCellValue("Status");
        headerRow.createCell(9).setCellValue("Supplier");
        headerRow.createCell(10).setCellValue("Budget Subtype");
        headerRow.createCell(11).setCellValue("Budget Type");
        headerRow.createCell(12).setCellValue("Document Number");
        headerRow.createCell(13).setCellValue("Invoice Description");

        if (movements.isEmpty()) {
            Row emptyRow = sheet.createRow(rowIdx++);
            emptyRow.createCell(0).setCellValue("No data available for the selected period.");
            return;
        }

        for (Movement movement : movements) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(movement.getId() != null ? movement.getId().toString() : "N/A");
            row.createCell(1).setCellValue(movement.getType() != null ? movement.getType().name() : "N/A");
            row.createCell(2).setCellValue(movement.getDateOfEmission() != null ? movement.getDateOfEmission().toString() : "N/A");
            row.createCell(3).setCellValue(movement.getDescription() != null ? movement.getDescription() : "N/A");
            row.createCell(4).setCellValue(movement.getValueWithoutIva() != null ? movement.getValueWithoutIva() : 0.0);
            row.createCell(5).setCellValue(movement.getIvaValue() != null ? movement.getIvaValue() : 0.0);
            row.createCell(6).setCellValue(movement.getIvaRate() != null ? movement.getIvaRate() : 0.0);
            row.createCell(7).setCellValue(movement.getTotalValue() != null ? movement.getTotalValue() : 0.0);
            row.createCell(8).setCellValue(movement.getStatus() != null ? movement.getStatus().name() : "N/A");
            row.createCell(9).setCellValue(movement.getSupplier() != null ? movement.getSupplier().getCompanyName() : "N/A");
            row.createCell(10).setCellValue(movement.getBudgetSubtype() != null ? movement.getBudgetSubtype().getName() : "N/A");
            row.createCell(11).setCellValue(movement.getBudgetType() != null ? movement.getBudgetType().getName() : "N/A");
            row.createCell(12).setCellValue(movement.getDocumentNumber() != null ? movement.getDocumentNumber() : "N/A");
            row.createCell(13).setCellValue(movement.getInvoice() != null ? movement.getInvoice().getDescription() : "N/A");
        }
    }

    public static List<Movement> filterMovements(MovementRepository movementRepository, LocalDate startDate, LocalDate endDate, MovementStatus status) {
        int conditionCode = (startDate != null ? 1 : 0) + (endDate != null ? 2 : 0) + (status != null ? 4 : 0);

        return switch (conditionCode) {
            case 7 -> movementRepository.findByDateOfEmissionBetweenAndStatus(startDate, endDate, status);
            case 3 -> movementRepository.findByDateOfEmissionBetween(startDate, endDate);
            case 4 -> movementRepository.findByStatus(status);
            default -> movementRepository.findAll();
        };
    }
}