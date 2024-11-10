package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.*;
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
import com.budgetMicroservice.util.PageableUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final MovementService movementService;
    private final InvoiceMapper invoiceMapper;
    private final KafkaTemplate<String, InvoiceDTO> kafkaInvoiceTemplate;
    private final KafkaTemplate<String, CustomPageDTO> kafkaCustomPageTemplate;

    @Override
    @KafkaListener(topics = "create-invoice", groupId = "invoice_group", concurrency = "10", containerFactory = "invoiceKafkaListenerContainerFactory")
    public InvoiceDTO create(InvoiceDTO invoiceDTO) throws InvoiceAlreadyExistsException, MovementNotFoundException {
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);

        if (invoiceDTO.getMovementId() != null) {
            Movement movement = movementService.getMovementEntityById(invoiceDTO.getMovementId());
            invoice.setMovement(movement);
        }

        Invoice savedInvoice = invoiceRepository.save(invoice);
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
            throw new InvoiceNotFoundException(id);
        }
        invoiceRepository.deleteById(id);
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
            throw new FailedToUploadFileException(request.getId());
        }

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
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(id));
    }
}