package com.budgetMicroservice.validator;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.exception.MovementNotFoundException;
import com.budgetMicroservice.exception.MovementValidationException;
import com.budgetMicroservice.repository.MovementRepository;
import com.budgetMicroservice.service.InvoiceService;
import com.budgetMicroservice.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MovementValidator {
    private final KafkaTemplate<String, MovementValidationException> kafkaMovementValidationExceptionTemplate;

    public void validateMovement(MovementDTO movementDTO,
                                 MovementRepository repository,
                                 SupplierService supplierService,
                                 InvoiceService invoiceService)
            throws MovementValidationException {

        List<String> errorMessages = new ArrayList<>();

        log.info("Checking for existing document number: {}", movementDTO.getDocumentNumber());

        if (repository.existsByDocumentNumber(movementDTO.getDocumentNumber())) {
            log.error("Validation failed: Document number already exists. Document number: {}", movementDTO.getDocumentNumber());
            errorMessages.add("a movement already exists with document number: " + movementDTO.getDocumentNumber());
        }

        log.info("Checking for existing movement with invoice ID: {}", movementDTO.getInvoiceId());

        if (repository.existsByInvoiceId(movementDTO.getInvoiceId())) {
            log.error("Validation failed: Movement already exists with invoice ID {}", movementDTO.getInvoiceId());
            errorMessages.add("a movement already exists with invoice ID: " + movementDTO.getInvoiceId());
        }

        if (!supplierService.existsById(movementDTO.getSupplierId())) {
            log.error("Validation failed: Supplier with ID {} not found.", movementDTO.getSupplierId());
            errorMessages.add("supplier with ID " + movementDTO.getSupplierId() + " not found.");
        }

        if (!invoiceService.existsById(movementDTO.getInvoiceId())) {
            log.error("Validation failed: Invoice with ID {} not found.", movementDTO.getInvoiceId());
            errorMessages.add("invoice with ID " + movementDTO.getInvoiceId() + " not found.");
        }

        if (!errorMessages.isEmpty()) {
            log.error("Movement validation failed with errors: {}", errorMessages);
            kafkaMovementValidationExceptionTemplate.send("movement-validation-exception-response", new MovementValidationException(errorMessages, movementDTO.getCorrelationId()));
            throw new MovementValidationException(errorMessages);
        }
    }


    public void validateMovementUpdate(MovementDTO movementDTO,
                                       MovementRepository repository,
                                       SupplierService supplierService,
                                       InvoiceService invoiceService)
            throws MovementValidationException {

        List<String> errorMessages = new ArrayList<>();

        log.info("Checking for existing document number (update): {}", movementDTO.getDocumentNumber());

        if (repository.existsByDocumentNumberAndIdNot(movementDTO.getDocumentNumber(), movementDTO.getId())) {
            log.error("Validation failed: Document number already exists. Document number: {}", movementDTO.getDocumentNumber());
            errorMessages.add("a movement already exists with document number: " + movementDTO.getDocumentNumber());
        }

        log.info("Checking for existing movement with invoice ID (update): {}", movementDTO.getInvoiceId());

        if (repository.existsByInvoiceIdAndIdNot(movementDTO.getInvoiceId(), movementDTO.getId())) {
            log.error("Validation failed: Movement already exists with invoice ID {}", movementDTO.getInvoiceId());
            errorMessages.add("a movement already exists with invoice ID: " + movementDTO.getInvoiceId());
        }

        if (!supplierService.existsById(movementDTO.getSupplierId())) {
            log.error("Validation failed: Supplier with ID {} not found.", movementDTO.getSupplierId());
            errorMessages.add("supplier with ID " + movementDTO.getSupplierId() + " not found.");
        }

        if (!invoiceService.existsById(movementDTO.getInvoiceId())) {
            log.error("Validation failed: Invoice with ID {} not found.", movementDTO.getInvoiceId());
            errorMessages.add("invoice with ID " + movementDTO.getInvoiceId() + " not found.");
        }

        if (!errorMessages.isEmpty()) {
            log.error("Movement validation failed with errors: {}", errorMessages);
            kafkaMovementValidationExceptionTemplate.send("movement-validation-exception-response", new MovementValidationException(errorMessages, movementDTO.getCorrelationId()));
            throw new MovementValidationException(errorMessages);
        }
    }

    public void validateMovementValues(MovementDTO movementDTO) throws MovementValidationException {
        List<String> errorMessages = new ArrayList<>();

        if (movementDTO.getValueWithoutIva() <= 0) {
            errorMessages.add("value without IVA must be greater than 0");
        }

        if (movementDTO.getIvaRate() < 0) {
            errorMessages.add("IVA rate must be greater than or equal to 0 if provided");
        }

        if (!errorMessages.isEmpty()) {
            log.error("Movement validation failed with errors: {}", errorMessages);
            kafkaMovementValidationExceptionTemplate.send("movement-validation-exception-response", new MovementValidationException(errorMessages, movementDTO.getCorrelationId()));
            throw new MovementValidationException(errorMessages);
        }
    }
}