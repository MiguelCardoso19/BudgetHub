package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.*;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.mapper.InvoiceMapper;
import com.budgetMicroservice.model.Invoice;
import com.budgetMicroservice.model.Movement;
import com.budgetMicroservice.repository.InvoiceRepository;
import com.budgetMicroservice.service.InvoiceService;
import com.budgetMicroservice.service.MovementService;
import com.budgetMicroservice.util.PageableUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final MovementService movementService;
    private final InvoiceMapper invoiceMapper;
    private final KafkaTemplate<String, InvoiceDTO> kafkaInvoiceTemplate;
    private final KafkaTemplate<String, CustomPageDTO> kafkaCustomPageTemplate;
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, InvoiceNotFoundException> kafkaInvoiceNotFoundExceptionTemplate;
    private final KafkaTemplate<String, FailedToUploadFileException> kafkaFailedToUploadFileExceptionTemplate;

    @Override
    @Transactional
    @KafkaListener(topics = "create-invoice", groupId = "invoice_group", concurrency = "10", containerFactory = "invoiceKafkaListenerContainerFactory")
    public InvoiceDTO create(InvoiceDTO invoiceDTO) throws InvoiceAlreadyExistsException, MovementNotFoundException, DocumentNumberNotFoundException {
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
        Movement movement = null;

        if (invoiceDTO.getMovementId() != null) {
            movement = movementService.getMovementEntityById(invoiceDTO.getMovementId());
            invoice.setMovement(movement);
        } else if (invoiceDTO.getMovementDocumentNumber() != null) {
            movement = movementService.getMovementByDocumentNumber(invoiceDTO.getMovementDocumentNumber());
            invoice.setMovement(movement);
        }

        Invoice savedInvoice = invoiceRepository.save(invoice);

        if (movement != null) {
            movement.setInvoice(savedInvoice);
        }

        InvoiceDTO savedInvoiceDTO = invoiceMapper.toDTO(savedInvoice);
        savedInvoiceDTO.setCorrelationId(invoiceDTO.getCorrelationId());
        kafkaInvoiceTemplate.send("invoice-response", savedInvoiceDTO);
        return savedInvoiceDTO;
    }

    @Override
    @KafkaListener(topics = "update-invoice", groupId = "invoice_group", concurrency = "10", containerFactory = "invoiceKafkaListenerContainerFactory")
    public InvoiceDTO update(InvoiceDTO invoiceDTO) throws InvoiceNotFoundException {
        findById(invoiceDTO.getId());
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
        InvoiceDTO savedInvoiceDTO = invoiceMapper.toDTO(invoiceRepository.save(invoice));
        kafkaInvoiceTemplate.send("invoice-response", savedInvoiceDTO);
        return savedInvoiceDTO;
    }

    @Override
    @KafkaListener(topics = "delete-invoice", groupId = "uuid_group", concurrency = "10")
    public void delete(UUID id) throws InvoiceNotFoundException {
        if (!invoiceRepository.existsById(id)) {
            kafkaInvoiceNotFoundExceptionTemplate.send("invoice-not-found-exception-response", new InvoiceNotFoundException(id));
            throw new InvoiceNotFoundException(id);
        }
        invoiceRepository.deleteById(id);
        kafkaUuidTemplate.send("invoice-delete-success-response", id);
    }

    @Override
    @Transactional
    @KafkaListener(topics = "get-all-invoices", groupId = "pageable_group", concurrency = "10", containerFactory = "customPageableKafkaListenerContainerFactory")
    public Page<InvoiceDTO> getAll(CustomPageableDTO customPageableDTO) {
        Page<Invoice> invoicePage = invoiceRepository.findAll(PageableUtils.convertToPageable(customPageableDTO));
        List<MovementDTO> invoiceDTOs = invoiceMapper.toDTOList(invoicePage);
        kafkaCustomPageTemplate.send("invoice-page-response", PageableUtils.buildCustomPageDTO(customPageableDTO, invoiceDTOs, invoicePage));
        return invoicePage.map(invoiceMapper::toDTO);
    }

    @Override
    @KafkaListener(topics = "get-by-id-invoice", groupId = "uuid_group", concurrency = "10")
    public InvoiceDTO findInvoiceDTOById(UUID id) throws InvoiceNotFoundException {
        InvoiceDTO invoiceDTO = invoiceMapper.toDTO(findById(id));
        kafkaInvoiceTemplate.send("invoice-response", invoiceDTO);
        return invoiceDTO;
    }

    public void attachMultipartFileToInvoice(UUID id, MultipartFile file) throws InvoiceNotFoundException, FailedToUploadFileException {
        Invoice invoice = findById(id);

        try {
            invoice.setFile(file.getBytes());
        } catch (IOException e) {
            throw new FailedToUploadFileException(id);
        }

        invoiceRepository.save(invoice);
    }

    @Override
    @KafkaListener(topics = "attach-base64-file-to-invoice", groupId = "attach_file_group", concurrency = "10", containerFactory = "attachFileRequestKafkaListenerContainerFactory")
    public void attachBase64FileToInvoice(AttachFileRequestDTO request) throws InvoiceNotFoundException, FailedToUploadFileException {
        Invoice invoice = findById(request.getId());

        try {
            byte[] fileBytes = Base64.getDecoder().decode(request.getBase64File());
            invoice.setFile(fileBytes);
        } catch (IllegalArgumentException e) {
            kafkaFailedToUploadFileExceptionTemplate.send("failed-to-upload-file-exception-response", new FailedToUploadFileException(request.getId()));
            throw new FailedToUploadFileException(request.getId());
        }

        kafkaUuidTemplate.send("upload-file-success-response", request.getId());
        invoiceRepository.save(invoice);
    }

    @Override
    public Invoice findInvoiceEntityById(UUID id) throws InvoiceNotFoundException {
        return findById(id);
    }

    @Override
    public boolean existsById(UUID invoiceId) {
        return invoiceRepository.existsById(invoiceId);
    }

    private Invoice findById(UUID id) throws InvoiceNotFoundException {
        Optional<Invoice> invoice = invoiceRepository.findById(id);

        if (invoice.isPresent()) {
            return invoice.get();
        }

        kafkaInvoiceNotFoundExceptionTemplate.send("invoice-not-found-exception-response", new InvoiceNotFoundException(id));
        throw new InvoiceNotFoundException(id);
    }
}