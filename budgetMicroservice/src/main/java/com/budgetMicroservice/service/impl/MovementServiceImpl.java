package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.*;
import com.budgetMicroservice.enumerator.MovementStatus;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.mapper.MovementMapper;
import com.budgetMicroservice.model.*;
import com.budgetMicroservice.repository.MovementRepository;
import com.budgetMicroservice.service.*;
import com.budgetMicroservice.util.MovementUtils;
import com.budgetMicroservice.util.PageableUtils;
import com.budgetMicroservice.validator.MovementValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.*;

import static com.budgetMicroservice.enumerator.MovementStatus.PAID;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class MovementServiceImpl implements MovementService {
    private final BudgetSubtypeService budgetSubtypeService;
    private final BudgetTypeService budgetTypeService;
    private final SupplierService supplierService;
    private final InvoiceService invoiceService;
    private final MovementValidator movementValidator;
    private final MovementUtils movementUtils;
    private final MovementMapper movementMapper;
    private final MovementRepository movementRepository;
    private final KafkaTemplate<String, MovementDTO> kafkaMovementTemplate;
    private final KafkaTemplate<String, CustomPageDTO> kafkaCustomPageTemplate;
    private final KafkaTemplate<String, NotificationRequestDTO> kafkaNotificationRequestTemplate;
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, MovementNotFoundException> kafkaMovementNotFoundExceptionTemplate;
    private final KafkaTemplate<String, MovementsNotFoundForBudgetTypeException> kafkaMovementsNotFoundForBudgetTypeExceptionTemplate;
    private final KafkaTemplate<String, MovementsNotFoundForBudgetSubtypeException> kafkaMovementsNotFoundForBudgetSubtypeExceptionTemplate;
    private final KafkaTemplate<String, GenerateExcelException> kafkaGenerateExcelExceptionTemplate;

    @Override
    @Transactional
    @KafkaListener(topics = "create-movement", groupId = "movement_group", concurrency = "10", containerFactory = "movementKafkaListenerContainerFactory")
    public MovementDTO create(MovementDTO movementDTO) throws BudgetSubtypeNotFoundException, SupplierNotFoundException, MovementAlreadyExistsException, MovementValidationException, InvoiceNotFoundException, BudgetExceededException, BudgetTypeNotFoundException {
        movementValidator.validateMovement(movementDTO, movementRepository, supplierService, invoiceService);
        movementUtils.calculateIvaAndTotal(movementDTO);
        Movement movement = movementMapper.toEntity(movementDTO);
        movement.setSupplier(supplierService.findSupplierEntityById(movementDTO.getSupplierId()));
        movement.setInvoice(invoiceService.findInvoiceEntityById(movementDTO.getInvoiceId()));
        MovementUtils.setBudget(movement, movementDTO, budgetSubtypeService, budgetTypeService);

        if (movement.getStatus().equals(PAID)) {
            movementUtils.updateSpentAmounts(movementDTO, budgetSubtypeService, budgetTypeService, movement, movement.getTotalValue());
        }

        Movement savedMovement = movementRepository.save(movement);
        MovementDTO savedMovementDTO = movementMapper.toDTO(savedMovement);
        savedMovementDTO.setCorrelationId(movementDTO.getCorrelationId());
        kafkaMovementTemplate.send("movement-response", savedMovementDTO);
        return savedMovementDTO;
    }

    @Override
    @Transactional
    @KafkaListener(topics = "update-movement", groupId = "movement_group", concurrency = "10", containerFactory = "movementKafkaListenerContainerFactory")
    public MovementDTO update(MovementDTO movementDTO) throws MovementNotFoundException, SupplierNotFoundException, BudgetSubtypeNotFoundException, MovementAlreadyExistsException, MovementValidationException, InvoiceNotFoundException, BudgetExceededException, BudgetTypeNotFoundException {
        Movement previousMovement = findById(movementDTO.getId());
        movementValidator.validateMovementUpdate(movementDTO, movementRepository, supplierService, invoiceService);
        movementUtils.calculateIvaAndTotal(movementDTO);

        if (movementDTO.getStatus().equals(PAID)) {
            MovementUtils.adjustBudgetAmounts(budgetSubtypeService, budgetTypeService, previousMovement, movementDTO);
        }

        Movement existingMovement = movementMapper.toEntity(movementDTO);
        existingMovement.setSupplier(supplierService.findSupplierEntityById(movementDTO.getSupplierId()));
        existingMovement.setInvoice(invoiceService.findInvoiceEntityById(movementDTO.getInvoiceId()));
        MovementUtils.setBudget(existingMovement, movementDTO, budgetSubtypeService, budgetTypeService);
        MovementDTO exisitingMovementDTO = movementMapper.toDTO(movementRepository.save(existingMovement));
        kafkaMovementTemplate.send("movement-response", exisitingMovementDTO);
        return exisitingMovementDTO;
    }

    @Override
    public Movement getMovementEntityById(UUID id) throws MovementNotFoundException {
        return findById(id);
    }

    @Override
    @KafkaListener(topics = "get-movement-by-id", groupId = "uuid_group", concurrency = "10")
    public MovementDTO getMovementDTOById(UUID id) throws MovementNotFoundException {
        MovementDTO movementDTO = movementMapper.toDTO(findById(id));
        kafkaMovementTemplate.send("movement-response", movementDTO);
        return movementDTO;
    }

    @Override
    @KafkaListener(topics = "delete-movement", groupId = "uuid_group", concurrency = "10")
    public void delete(UUID id) throws MovementNotFoundException {
        Movement existingMovement = findById(id);

        if (existingMovement.getStatus().equals(PAID)) {
            MovementUtils.removeMovementValueFromBudget(existingMovement, budgetSubtypeService, budgetTypeService);
        }
        kafkaUuidTemplate.send("movement-delete-success-response", id);
        movementRepository.deleteById(id);
    }

    @Override
    @Transactional
    @KafkaListener(topics = "get-all-movements", groupId = "pageable_group", concurrency = "10", containerFactory = "customPageableKafkaListenerContainerFactory")
    public Page<MovementDTO> getAll(CustomPageableDTO customPageableDTO) {
        Page<Movement> movementPage = movementRepository.findAll(PageableUtils.convertToPageable(customPageableDTO));
        List<MovementDTO> movementDTOs = movementMapper.toDTOList(movementPage);
        kafkaCustomPageTemplate.send("movement-page-response", PageableUtils.buildCustomPageDTO(customPageableDTO, movementDTOs, movementPage));

        return movementPage.map(movementMapper::toDTO);
    }

    @Transactional
    @Override
    @KafkaListener(topics = "get-movements-by-budget-type", groupId = "movement_group", concurrency = "10", containerFactory = "movementsByBudgetDTOKafkaListenerContainerFactory")
    public Page<MovementDTO> getMovementsByBudgetType(MovementsByBudgetRequestDTO movementsByBudgetRequestDTO) throws Exception {
        return getMovementsByBudgetId(movementsByBudgetRequestDTO, true, "MovementsNotFoundForBudgetTypeException");
    }

    @Transactional
    @Override
    @KafkaListener(topics = "get-movements-by-budget-subtype", groupId = "movements_by_budget_request_group", concurrency = "10", containerFactory = "movementsByBudgetDTOKafkaListenerContainerFactory")
    public Page<MovementDTO> getMovementsByBudgetSubtype(MovementsByBudgetRequestDTO movementsByBudgetRequestDTO) throws Exception {
        return getMovementsByBudgetId(movementsByBudgetRequestDTO, false, "MovementsNotFoundForBudgetSubtypeException");
    }

    /**
     * IMPLEMENT PAYMENT SERVICE
     */
    @Override
    @KafkaListener(topics = "update-movement-status", groupId = "movement_update_status_group", concurrency = "10", containerFactory = "movementUpdateStatusRequestKafkaContainerFactory")
    public MovementDTO updateMovementStatus(MovementUpdateStatusRequestDTO movementUpdateStatusRequestDTO) throws MovementNotFoundException, MovementValidationException, BudgetExceededException {
        Movement movement = findById(movementUpdateStatusRequestDTO.getId());
        movement.setStatus(movementUpdateStatusRequestDTO.getStatus());
        MovementDTO movementDTO = movementMapper.toDTO(movement);
        movementDTO.setCorrelationId(movementUpdateStatusRequestDTO.getCorrelationId());

        if (movementUpdateStatusRequestDTO.getStatus().equals(PAID)) {
            movementUtils.updateSpentAmounts(movementDTO, budgetSubtypeService, budgetTypeService, movement, movement.getTotalValue());
        }
        movementRepository.save(movement);
        MovementDTO movementDTOToSend = movementMapper.toDTO(movement);
        kafkaMovementTemplate.send("movement-response", movementDTOToSend);
        return movementDTOToSend;
    }

    @Override
    @KafkaListener(topics = "export-movements-report", groupId = "movement_group", concurrency = "10", containerFactory = "exportMovementsKafkaListenerContainerFactory")
    public void exportMovements(ExportMovementsRequestDTO request) throws GenerateExcelException {
        List<Movement> movements = MovementUtils.filterMovements(
                movementRepository,
                request.getStartDate(),
                request.getEndDate(),
                request.getStatus()
        );

        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            MovementUtils.populateSheetWithMovements(workbook.createSheet("Movements Report"), movements);
            workbook.write(outStream);
            String attachmentBase64 = Base64.getEncoder().encodeToString(outStream.toByteArray());
            kafkaNotificationRequestTemplate.send("notification-topic", new NotificationRequestDTO(request.getUserEmail(), attachmentBase64));
            kafkaUuidTemplate.send("export-report-success-response", request.getCorrelationId());
        } catch (IOException e) {
            kafkaGenerateExcelExceptionTemplate.send("generate-excel-exception-response", new GenerateExcelException(request.getCorrelationId()));
            throw new GenerateExcelException();
        }
    }

    /**
     * IMPLEMENT PAYMENT SERVICE
     */
    @Override
    @KafkaListener(topics = "movement-status-request", groupId = "uuid_group", concurrency = "10")
    public MovementStatus getMovementStatus(UUID id) throws MovementNotFoundException {
        Movement movement = findById(id);
        return movement.getStatus();
    }

    private Page<MovementDTO> getMovementsByBudgetId(MovementsByBudgetRequestDTO requestDTO, boolean isBudgetType, String exceptionType) throws Exception {
        Pageable pageable = PageableUtils.convertToPageable(requestDTO.getPageable());
        Page<Movement> movementPage;

        if (isBudgetType) {
            movementPage = movementRepository.findByBudgetTypeId(requestDTO.getId(), pageable);
        } else {
            movementPage = movementRepository.findByBudgetSubtypeId(requestDTO.getId(), pageable);
        }

        if (movementPage.isEmpty()) {
            if ("MovementsNotFoundForBudgetTypeException".equals(exceptionType)) {
                kafkaMovementsNotFoundForBudgetTypeExceptionTemplate.send("movement-not-found-for-budget-type-exception-response", new MovementsNotFoundForBudgetTypeException(requestDTO.getCorrelationId(), requestDTO.getId()));
                throw new MovementsNotFoundForBudgetTypeException(requestDTO.getId());
            } else if ("MovementsNotFoundForBudgetSubtypeException".equals(exceptionType)) {
                kafkaMovementsNotFoundForBudgetSubtypeExceptionTemplate.send("movement-not-found-for-budget-subtype-exception-response", new MovementsNotFoundForBudgetSubtypeException(requestDTO.getCorrelationId(), requestDTO.getId()));
                throw new MovementsNotFoundForBudgetSubtypeException(requestDTO.getId());
            }
        }

        List<MovementDTO> movementDTOs = movementMapper.toDTOList(movementPage);
        kafkaCustomPageTemplate.send("movement-page-response", PageableUtils.buildCustomPageDTO(requestDTO.getPageable(), movementDTOs, movementPage));
        return movementPage.map(isBudgetType ? movementMapper::toDTOWithoutBudgetType : movementMapper::toDTOWithoutBudgetSubtype);
    }

    private Movement findById(UUID id) throws MovementNotFoundException {
        Optional<Movement> movement = movementRepository.findById(id);

        if (movement.isPresent()) {
            return movement.get();
        }

        kafkaMovementNotFoundExceptionTemplate.send("movement-not-found-exception-response", new MovementNotFoundException(id));
        throw new MovementNotFoundException(id);
    }
}