package com.budgetMicroservice.validator;

import com.budgetMicroservice.dto.InvoiceDTO;
import com.budgetMicroservice.exception.InvoiceAlreadyExistsException;
import com.budgetMicroservice.repository.InvoiceRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvoiceValidator {

    public static void validateDocumentNumberForExistingInvoice(InvoiceDTO invoiceDTO,
                                                                InvoiceRepository repository)
            throws InvoiceAlreadyExistsException {
        log.info("Checking for existing document number: {}", invoiceDTO.getDocumentNumber());

        if (repository.existsByDocumentNumber(invoiceDTO.getDocumentNumber())) {
            log.error("Validation failed: Document number already exists. Document number: {}", invoiceDTO.getDocumentNumber());
            throw new InvoiceAlreadyExistsException(invoiceDTO.getDocumentNumber());
        }
    }

    public static void validateDocumentNumberForExistingInvoiceUpdate(InvoiceDTO invoiceDTO,
                                                                      InvoiceRepository repository)
            throws InvoiceAlreadyExistsException {
        log.info("Checking for existing document number (update): {}", invoiceDTO.getDocumentNumber());

        if (repository.existsByDocumentNumberAndIdNot(invoiceDTO.getDocumentNumber(), invoiceDTO.getId())) {
            log.error("Validation failed: Document number already exists. Document number: {}", invoiceDTO.getDocumentNumber());
            throw new InvoiceAlreadyExistsException(invoiceDTO.getDocumentNumber());
        }
    }
}