package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.enumerator.MovementStatus;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.mapper.MovementMapper;
import com.budgetMicroservice.model.*;
import com.budgetMicroservice.repository.MovementRepository;
import com.budgetMicroservice.service.*;
import com.budgetMicroservice.util.MovementUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static com.budgetMicroservice.enumerator.MovementStatus.PAID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class MovementServiceImpl implements MovementService {
    private final BudgetSubtypeService budgetSubtypeService;
    private final BudgetTypeService budgetTypeService;
    private final SupplierService supplierService;
    private final InvoiceService invoiceService;
    private final MovementMapper movementMapper;
    private final MovementRepository movementRepository;
  //  private final KafkaTemplate<String, String> kafkaStringTemplate;
   // private final KafkaTemplate<String, MovementDTO> kafkaMovementTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
   // @KafkaListener(topics = "create-movement", groupId = "movement_group", concurrency = "10")
    public MovementDTO create(MovementDTO movementDTO) throws BudgetSubtypeNotFoundException, SupplierNotFoundException, MovementAlreadyExistsException, MovementValidationException, InvoiceNotFoundException {
        Supplier supplier = supplierService.findSupplierEntityById(movementDTO.getSupplierId());
        Invoice invoice = invoiceService.findInvoiceEntityById(movementDTO.getInvoiceId());

        MovementUtils.calculateIvaAndTotal(movementDTO);

        Movement movement = movementMapper.toEntity(movementDTO);
        movement.setSupplier(supplier);
        movement.setInvoice(invoice);

        MovementUtils.setBudget(movement, movementDTO, budgetSubtypeService, budgetTypeService);

        if (movement.getStatus().equals(PAID)) {
            MovementUtils.updateSpentAmounts(budgetSubtypeService, budgetTypeService, movement, movement.getTotalValue());
        }

        Movement savedMovement = movementRepository.save(movement);
        MovementDTO savedMovementDTO = movementMapper.toDTO(savedMovement);
    //    kafkaMovementTemplate.send("movement-created", savedMovementDTO);

        return savedMovementDTO;
    }

    @Override
    @Transactional
 //   @KafkaListener(topics = "update-movement", groupId = "movement_group", concurrency = "10")
    public MovementDTO update(MovementDTO movementDTO) throws MovementNotFoundException, SupplierNotFoundException, BudgetSubtypeNotFoundException, MovementAlreadyExistsException, MovementValidationException, InvoiceNotFoundException {
        Movement existingMovement = findById(movementDTO.getId());
        Supplier existingSupplier = supplierService.findSupplierEntityById(movementDTO.getSupplierId());
        Invoice invoice = invoiceService.findInvoiceEntityById(movementDTO.getInvoiceId());

        MovementUtils.calculateIvaAndTotal(movementDTO);

        if (existingMovement.getStatus().equals(PAID)) {
            MovementUtils.adjustBudgetAmounts(budgetSubtypeService, budgetTypeService, existingMovement, movementDTO);
        }

        movementMapper.updateFromDTO(movementDTO, existingMovement);
        existingMovement.setSupplier(existingSupplier);
        existingMovement.setInvoice(invoice);
        MovementUtils.setBudget(existingMovement, movementDTO, budgetSubtypeService, budgetTypeService);

        movementRepository.save(existingMovement);
        MovementDTO exisitingMovementDTO = movementMapper.toDTO(existingMovement);
   //     kafkaMovementTemplate.send("movement-updated", exisitingMovementDTO);

        return exisitingMovementDTO;
    }

    @Override
    public Movement getMovementEntityById(UUID id) throws MovementNotFoundException {
        return findById(id);
    }

    @Override
  //  @KafkaListener(topics = "get-movement", groupId = "movement_group", concurrency = "10")
    public MovementDTO getMovementDTOById(UUID id) throws MovementNotFoundException {
        MovementDTO movementDTO = movementMapper.toDTO(findById(id));
   //     kafkaMovementTemplate.send("get-movement", movementDTO);
        return movementDTO;
    }

    @Override
//    @KafkaListener(topics = "delete-movement", groupId = "movement_group", concurrency = "10")
    public void delete(UUID id) throws MovementNotFoundException {
        Movement existingMovement = findById(id);

        if (existingMovement.getStatus().equals(PAID)) {
            MovementUtils.removeMovementValueFromBudget(existingMovement, budgetSubtypeService, budgetTypeService);
        }

        movementRepository.deleteById(id);
    }

    @Override
 //   @KafkaListener(topics = "get-all-movements", groupId = "movement_group", concurrency = "10")
    public Page<MovementDTO> getAll(Pageable pageable) throws JsonProcessingException {
        Page<MovementDTO> movements = movementRepository.findAll(pageable).map(movementMapper::toDTO);

     //   kafkaStringTemplate.send("get-all-movements", objectMapper.writeValueAsString(movements));
        return movements;
    }

    @Override
  //  @KafkaListener(topics = "get-movements-by-budget-type", groupId = "movement_group", concurrency = "10")
    public Page<MovementDTO> getMovementsByBudgetType(UUID budgetTypeId, Pageable pageable) throws MovementsNotFoundForBudgetTypeException, JsonProcessingException {
        Page<Movement> movements = movementRepository.findByBudgetTypeId(budgetTypeId, pageable);
        if (movements.isEmpty()) {
            throw new MovementsNotFoundForBudgetTypeException(budgetTypeId);
        }

    //    kafkaStringTemplate.send("get-movements-by-budget-type", objectMapper.writeValueAsString(movements));
        return movements.map(movementMapper::toDTOWithoutBudgetType);
    }

    @Override
  //  @KafkaListener(topics = "get-movements-by-budget-subtype", groupId = "movement_group", concurrency = "10")
    public Page<MovementDTO> getMovementsByBudgetSubtype(UUID budgetSubtypeId, Pageable pageable) throws MovementsNotFoundForBudgetSubtypeException, JsonProcessingException {
        Page<Movement> movements = movementRepository.findByBudgetSubtypeId(budgetSubtypeId, pageable);

        if (movements.isEmpty()) {
            throw new MovementsNotFoundForBudgetSubtypeException(budgetSubtypeId);
        }

     //   kafkaStringTemplate.send("get-movements-by-budget-subtype", objectMapper.writeValueAsString(movements));
        return movements.map(movementMapper::toDTOWithoutBudgetSubtype);
    }

    @Override
  //  @KafkaListener(topics = "update-movement-status", groupId = "movement_group", concurrency = "10")
    public MovementDTO updateMovementStatus(UUID id, MovementStatus status) throws MovementNotFoundException {
        Movement movement = findById(id);
        movement.setStatus(status);

        if (status.equals(PAID)) {
            MovementUtils.updateSpentAmounts(budgetSubtypeService, budgetTypeService, movement, movement.getTotalValue());
        }
        movementRepository.save(movement);

        MovementDTO movementDTO = movementMapper.toDTO(movement);
    //    kafkaMovementTemplate.send("update-movement-status", movementDTO);
        return movementDTO;
    }

    @Override
  //  @KafkaListener(topics = "export-movements-report", groupId = "movement_group", concurrency = "10")
    public void exportAndSendMovements(LocalDate startDate, LocalDate endDate, MovementStatus status, String userEmail) throws MovementNotFoundException, GenerateExcelException {
        List<Movement> movements = MovementUtils.filterMovements(movementRepository, startDate, endDate, status);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Movements Report");
            MovementUtils.populateSheetWithMovements(sheet, movements);

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            workbook.write(outStream);

            String attachmentBase64 = Base64.getEncoder().encodeToString(outStream.toByteArray());
            String message = String.format("{\"email\":\"%s\", \"attachment\":\"%s\"}", userEmail, attachmentBase64);

         //   kafkaStringTemplate.send("email-notification-topic", message);
        } catch (IOException e) {
            throw new GenerateExcelException();
        }
    }

    @Override
  //  @KafkaListener(topics = "movement-status-request", groupId = "status_group", concurrency = "10")
    public MovementStatus getMovementStatus(UUID id) throws MovementNotFoundException {
        Movement movement = findById(id);
  //      kafkaStringTemplate.send("movement-status-response", String.format("{\"id\":\"%s\", \"status\":\"%s\"}", id.toString(), movement.getStatus()));
        return movement.getStatus();
    }

    private Movement findById(UUID id) throws MovementNotFoundException {
        return movementRepository.findById(id).orElseThrow(() -> new MovementNotFoundException(id));
    }
}