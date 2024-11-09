package com.portalMicroservice.client.budget;

import com.portalMicroservice.config.CustomErrorDecoder;
import com.portalMicroservice.dto.budget.InvoiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@FeignClient(name = "invoiceFeignClient", url = "${budget-microservice-invoice.url}", configuration = CustomErrorDecoder.class)
public interface InvoiceFeignClient {

    @PostMapping(value = "/attach-multipart-file/{invoiceId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> uploadFileToInvoice(@PathVariable("invoiceId") UUID invoiceId, @RequestPart("file") MultipartFile file);

    @PostMapping("/create")
    ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO invoiceDTO);

    @PutMapping("/update")
    ResponseEntity<InvoiceDTO> updateInvoice(@RequestBody InvoiceDTO invoiceDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteInvoice(@PathVariable UUID id);

    @GetMapping("/all")
    ResponseEntity<Page<InvoiceDTO>> getAllInvoices(Pageable pageable);

    @GetMapping("/{id}")
    ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable UUID id);
}