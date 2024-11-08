package com.portalMicroservice.controller.budget;

import com.portalMicroservice.client.budget.InvoiceFeignClient;
import com.portalMicroservice.dto.budget.AttachFileRequestDTO;
import com.portalMicroservice.dto.budget.InvoiceDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceFeignClient invoiceFeignClient;
    private final InvoiceService invoiceService;

    @PostMapping("/{invoiceId}/upload-file")
    public ResponseEntity<Void> uploadFileToInvoice(@PathVariable UUID invoiceId,
                                                    @RequestParam("file") MultipartFile file){
        return invoiceFeignClient.uploadFileToInvoice(invoiceId, file);
    }

    @PostMapping("/create")
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO invoiceDTO) throws GenericException {
         return ResponseEntity.ok(invoiceService.create(invoiceDTO));
      //  return invoiceFeignClient.createInvoice(invoiceDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<InvoiceDTO> updateInvoice(@RequestBody InvoiceDTO invoiceDTO) throws GenericException {
         return ResponseEntity.ok(invoiceService.update(invoiceDTO));
       // return invoiceFeignClient.updateInvoice(invoiceDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable UUID id) throws GenericException {
         invoiceService.delete(id);
         return ResponseEntity.noContent().build();
       // return invoiceFeignClient.deleteInvoice(id);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<InvoiceDTO>> getAllInvoices(Pageable pageable) {
     //    return ResponseEntity.ok(invoiceService.getAll(pageable));
       return invoiceFeignClient.getAllInvoices(pageable);
    }

    @PostMapping("/{invoice-id}/movement/{movement-id}")
    public ResponseEntity<InvoiceDTO> addMovementToInvoice(
            @PathVariable("invoice-id") UUID invoiceId,
            @PathVariable("movement-id") UUID movementId) {
       //  return ResponseEntity.ok(invoiceService.addMovementToInvoice(invoiceId, movementId));
        return invoiceFeignClient.addMovementToInvoice(invoiceId, movementId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable UUID id) throws GenericException {
         return ResponseEntity.ok(invoiceService.getById(id));
      //  return invoiceFeignClient.getInvoiceById(id);
    }

    @PostMapping("/attach-base64-file-to-invoice")
    public ResponseEntity<Void> attachBase64FileToInvoice(@RequestBody AttachFileRequestDTO attachFileRequestDTO) {
        invoiceService.attachBase64FileToInvoice(attachFileRequestDTO);
        return ResponseEntity.noContent().build();
    }
}