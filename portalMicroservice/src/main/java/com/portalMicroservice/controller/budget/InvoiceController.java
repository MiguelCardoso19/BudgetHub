package com.portalMicroservice.controller.budget;

import com.portalMicroservice.client.budget.InvoiceFeignClient;
import com.portalMicroservice.dto.budget.AttachFileRequestDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.InvoiceDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.budget.FailedToUploadFileException;
import com.portalMicroservice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceFeignClient invoiceFeignClient;
    private final InvoiceService invoiceService;

    // attach via feign client
    @PostMapping("/attach-multipart-file/{invoiceId}")
    public ResponseEntity<Void> attachMultipartFileToInvoice(@PathVariable UUID invoiceId, @RequestParam("file") MultipartFile file) {
        invoiceFeignClient.uploadFileToInvoice(invoiceId, file);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/attach-base64-file/{invoiceId}")
    public ResponseEntity<Void> attachBase64FileToInvoice(@PathVariable UUID invoiceId, @RequestParam("file") MultipartFile file) throws FailedToUploadFileException {
        invoiceService.attachBase64FileToInvoice(invoiceId, file);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create")
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO invoiceDTO) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        return ResponseEntity.ok(invoiceService.create(invoiceDTO));
        //  return invoiceFeignClient.createInvoice(invoiceDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<InvoiceDTO> updateInvoice(@RequestBody InvoiceDTO invoiceDTO) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
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
    public ResponseEntity<CustomPageDTO> getAllInvoices(Pageable pageable) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        return ResponseEntity.ok(invoiceService.getAll(pageable));
        //  return invoiceFeignClient.getAllInvoices(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        return ResponseEntity.ok(invoiceService.getById(id));
        //  return invoiceFeignClient.getInvoiceById(id);
    }
}