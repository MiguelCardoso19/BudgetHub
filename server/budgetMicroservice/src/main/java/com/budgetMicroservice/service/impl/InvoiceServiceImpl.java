package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.*;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.mapper.InvoiceMapper;
import com.budgetMicroservice.model.Invoice;
import com.budgetMicroservice.model.Movement;
import com.budgetMicroservice.repository.InvoiceRepository;
import com.budgetMicroservice.service.S3Service;
import com.budgetMicroservice.util.Base64DecodedMultipartFile;
import com.budgetMicroservice.service.InvoiceService;
import com.budgetMicroservice.service.MovementService;
import com.budgetMicroservice.util.InvoiceUtils;
import com.budgetMicroservice.util.PageableUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final S3Service s3Service;
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
        Invoice existingInvoice = findById(invoiceDTO.getId());

        if (existingInvoice.getFileKey() != null && invoiceDTO.getFileKey() != null) {
            s3Service.deleteObject(existingInvoice.getFileKey());
        }

        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
        InvoiceDTO savedInvoiceDTO = invoiceMapper.toDTO(invoiceRepository.save(invoice));
        kafkaInvoiceTemplate.send("invoice-response", savedInvoiceDTO);
        return savedInvoiceDTO;
    }

    @Override
    @KafkaListener(topics = "delete-invoice", groupId = "uuid_group", concurrency = "10")
    public void delete(UUID id) throws InvoiceNotFoundException {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> {
            kafkaInvoiceNotFoundExceptionTemplate.send("invoice-not-found-exception-response", new InvoiceNotFoundException(id));
            return new InvoiceNotFoundException(id);
        });

        if (invoice.getFileKey() != null) {
            s3Service.deleteObject(invoice.getFileKey());
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
    public InvoiceDTO findInvoiceDTOById(UUID id) throws InvoiceNotFoundException, IOException {
        Invoice invoice = findById(id);
        InvoiceDTO invoiceDTO = invoiceMapper.toDTO(invoice);

        if (invoice.getFileKey() != null) {
            InputStream fileInputStream = s3Service.getObject(invoice.getFileKey());
            invoiceDTO.setFileBase64(Base64.getEncoder().encodeToString(fileInputStream.readAllBytes()));
        }

        kafkaInvoiceTemplate.send("invoice-response", invoiceDTO);
        return invoiceDTO;
    }

    @Override
    public void attachMultipartFileToInvoice(UUID id, MultipartFile file) throws InvoiceNotFoundException, FailedToUploadFileException {
        Invoice invoice = findById(id);

        if (invoice.getFileKey() != null) {
            s3Service.deleteObject(invoice.getFileKey());
        }

        try {
            String fileName = UUID.randomUUID() + InvoiceUtils.getFileExtensionFromContentType(Objects.requireNonNull(file.getContentType()));
            invoice.setFileKey(s3Service.putObject(new Base64DecodedMultipartFile(file.getBytes(), fileName, file.getContentType())));
        } catch (IOException e) {
            throw new FailedToUploadFileException(id);
        }
        invoiceRepository.save(invoice);
    }

    @Override
    @KafkaListener(topics = "attach-base64-file-to-invoice", groupId = "attach_file_group", concurrency = "10", containerFactory = "attachFileRequestKafkaListenerContainerFactory")
    public void attachBase64FileToInvoice(AttachFileRequestDTO request) throws InvoiceNotFoundException, FailedToUploadFileException {
        Invoice invoice = findById(request.getId());

        if (invoice.getFileKey() != null) {
            s3Service.deleteObject(invoice.getFileKey());
        }

        try {
            byte[] fileBytes = Base64.getDecoder().decode(request.getBase64File());
            String fileName = UUID.randomUUID() + InvoiceUtils.getFileExtensionFromContentType(request.getContentType());
            String fileKey = s3Service.putObject(new Base64DecodedMultipartFile(fileBytes, fileName, request.getContentType()));
            invoice.setFileKey(fileKey);
        } catch (IllegalArgumentException | IOException e) {
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