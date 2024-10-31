package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.InvoiceDTO;
import com.budgetMicroservice.exception.FailedToUploadFileException;
import com.budgetMicroservice.exception.InvoiceAlreadyExistsException;
import com.budgetMicroservice.exception.InvoiceNotFoundException;
import com.budgetMicroservice.exception.MovementNotFoundException;
import com.budgetMicroservice.mapper.InvoiceMapper;
import com.budgetMicroservice.model.Invoice;
import com.budgetMicroservice.model.Movement;
import com.budgetMicroservice.repository.InvoiceRepository;
import com.budgetMicroservice.service.InvoiceService;
import com.budgetMicroservice.service.MovementService;
import com.budgetMicroservice.validator.InvoiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final MovementService movementService;
    private final InvoiceMapper invoiceMapper;

    @Override
    public InvoiceDTO attachFileToInvoice(UUID id, MultipartFile file) throws InvoiceNotFoundException, FailedToUploadFileException {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(id));

        try {
            invoice.setFile(file.getBytes());
        } catch (IOException e) {
            throw new FailedToUploadFileException(id);
        }

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toDTO(updatedInvoice);
    }

    @Override
    public InvoiceDTO create(InvoiceDTO invoiceDTO) throws InvoiceAlreadyExistsException, MovementNotFoundException {
        InvoiceValidator.validateDocumentNumberForExistingInvoice(invoiceDTO, invoiceRepository);

        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);

        if (invoiceDTO.getMovementId() != null) {
            Movement movement = movementService.getMovementEntityById(invoiceDTO.getMovementId());
            invoice.setMovement(movement);
        }

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toDTO(savedInvoice);
    }

    @Override
    public InvoiceDTO addMovementToInvoice(UUID invoiceId, UUID movementId) throws InvoiceNotFoundException, MovementNotFoundException {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));

        Movement movement = movementService.getMovementEntityById(movementId);
        invoice.setMovement(movement);

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toDTO(updatedInvoice);
    }

    @Override
    public InvoiceDTO update(InvoiceDTO invoiceDTO) throws InvoiceNotFoundException, InvoiceAlreadyExistsException {
        Invoice existingInvoice = invoiceRepository.findById(invoiceDTO.getId())
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceDTO.getId()));

        InvoiceValidator.validateDocumentNumberForExistingInvoiceUpdate(invoiceDTO, invoiceRepository);

        invoiceMapper.updateFromDTO(invoiceDTO, existingInvoice);
        Invoice savedInvoice = invoiceRepository.save(existingInvoice);
        return invoiceMapper.toDTO(savedInvoice);
    }

    @Override
    public void delete(UUID id) throws InvoiceNotFoundException {
        if (!invoiceRepository.existsById(id)) {
            throw new InvoiceNotFoundException(id);
        }
        invoiceRepository.deleteById(id);
    }

    @Override
    public Page<InvoiceDTO> getAll(Pageable pageable) {
        Page<Invoice> invoicesPage = invoiceRepository.findAll(pageable);
        return invoicesPage.map(invoiceMapper::toDTO);
    }

    @Override
    public InvoiceDTO getById(UUID id) throws InvoiceNotFoundException {
        return invoiceMapper.toDTO(invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(id)));
    }
}