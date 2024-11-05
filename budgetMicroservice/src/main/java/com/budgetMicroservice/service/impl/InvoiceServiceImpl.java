package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.AttachFileRequestDTO;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final MovementService movementService;
    private final InvoiceMapper invoiceMapper;
    private final KafkaTemplate<String, InvoiceDTO> kafkaInvoiceTemplate;
    private final KafkaTemplate<String, String> kafkaStringTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void attachFileToInvoice(UUID id, MultipartFile file) throws InvoiceNotFoundException, FailedToUploadFileException {
        Invoice invoice = findById(id);

        try {
            invoice.setFile(file.getBytes());
        } catch (IOException e) {
            throw new FailedToUploadFileException(id);
        }

        invoiceRepository.save(invoice);
    }

    @Override
    @KafkaListener(topics = "attach-base64-file-to-invoice", groupId = "attach_file_group", concurrency = "10")
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
    @KafkaListener(topics = "create-invoice", groupId = "invoice_group", concurrency = "10")
    public InvoiceDTO create(InvoiceDTO invoiceDTO) throws InvoiceAlreadyExistsException, MovementNotFoundException {
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);

        if (invoiceDTO.getMovementId() != null) {
            Movement movement = movementService.getMovementEntityById(invoiceDTO.getMovementId());
            invoice.setMovement(movement);
        }

        Invoice savedInvoice = invoiceRepository.save(invoice);
        InvoiceDTO savedInvoiceDTO = invoiceMapper.toDTO(savedInvoice);
        kafkaInvoiceTemplate.send("invoice-response", savedInvoiceDTO);

        return savedInvoiceDTO;
    }

    @Override
    @KafkaListener(topics = "add-movement-to-invoice", groupId = "invoice_group", concurrency = "10")
    public InvoiceDTO addMovementToInvoice(UUID invoiceId, UUID movementId) throws InvoiceNotFoundException, MovementNotFoundException {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));

        Movement movement = movementService.getMovementEntityById(movementId);
        invoice.setMovement(movement);

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        InvoiceDTO updatedInvoiceDTO = invoiceMapper.toDTO(updatedInvoice);
        kafkaInvoiceTemplate.send("invoice-response", updatedInvoiceDTO);

        return updatedInvoiceDTO;
    }

    @Override
    @KafkaListener(topics = "update-invoice", groupId = "invoice_group", concurrency = "10")
    public InvoiceDTO update(InvoiceDTO invoiceDTO) throws InvoiceNotFoundException, InvoiceAlreadyExistsException {
        Invoice existingInvoice = invoiceRepository.findById(invoiceDTO.getId())
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceDTO.getId()));

        invoiceMapper.updateFromDTO(invoiceDTO, existingInvoice);
        Invoice savedInvoice = invoiceRepository.save(existingInvoice);
        InvoiceDTO savedInvoiceDTO = invoiceMapper.toDTO(savedInvoice);
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
     @KafkaListener(topics = "get-all-invoices", groupId = "invoice_group", concurrency = "10")
    public Page<InvoiceDTO> getAll(Pageable pageable) throws JsonProcessingException {
        Page<Invoice> invoicesPage = invoiceRepository.findAll(pageable);
        kafkaStringTemplate.send("invoice-response", objectMapper.writeValueAsString(invoicesPage));
        return invoicesPage.map(invoiceMapper::toDTO);
    }

    @Override
    public InvoiceDTO findInvoiceInvoiceDTOById(UUID id) throws InvoiceNotFoundException {
        return invoiceMapper.toDTO(findById(id));
    }

    @Override
    public Invoice findInvoiceEntityById(UUID id) throws InvoiceNotFoundException {
        return findById(id);
    }

    private Invoice findById(UUID id) throws InvoiceNotFoundException {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(id));
    }
}
