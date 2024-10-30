package com.budgetMicroservice.controller;

import com.budgetMicroservice.dto.InvoiceDTO;
import com.budgetMicroservice.exception.FailedToUploadFileException;
import com.budgetMicroservice.exception.InvoiceAlreadyExistsException;
import com.budgetMicroservice.exception.InvoiceNotFoundException;
import com.budgetMicroservice.exception.MovementNotFoundException;
import com.budgetMicroservice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @PostMapping("/{invoiceId}/upload-file")
    public ResponseEntity<InvoiceDTO> uploadFileToInvoice(
            @PathVariable UUID invoiceId,
            @RequestParam("file") MultipartFile file) throws InvoiceNotFoundException, FailedToUploadFileException {

        return ResponseEntity.ok(invoiceService.attachFileToInvoice(invoiceId, file));
    }

    @PostMapping("/create")
    public ResponseEntity<InvoiceDTO> createInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO) throws InvoiceAlreadyExistsException, MovementNotFoundException {
        return ResponseEntity.ok(invoiceService.create(invoiceDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<InvoiceDTO> updateInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO)
            throws InvoiceNotFoundException, InvoiceAlreadyExistsException {
        return ResponseEntity.ok(invoiceService.update(invoiceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable UUID id) throws InvoiceNotFoundException {
        invoiceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<InvoiceDTO>> getAllInvoices(Pageable pageable) {
        return ResponseEntity.ok(invoiceService.getAll(pageable));
    }

    @PostMapping("/{invoice-id}/movement/{movement-id}")
    public ResponseEntity<InvoiceDTO> addMovementToInvoice(@PathVariable("invoice-id") UUID invoiceId, @PathVariable("movement-id") UUID movementId) throws MovementNotFoundException, InvoiceNotFoundException {
        return ResponseEntity.ok(invoiceService.addMovementToInvoice(invoiceId, movementId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getById(@PathVariable UUID id) throws InvoiceNotFoundException {
        return ResponseEntity.ok(invoiceService.getById(id));
    }
}