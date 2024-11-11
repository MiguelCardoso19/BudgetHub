package com.portalMicroservice.controller.budget;

import com.portalMicroservice.client.budget.InvoiceFeignClient;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.InvoiceDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.budget.FailedToUploadFileException;
import com.portalMicroservice.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Invoice Controller", description = "Operations related to Invoices")
public class InvoiceController {
    private final InvoiceFeignClient invoiceFeignClient;
    private final InvoiceService invoiceService;

    @Operation(summary = "Attach multipart file to invoice", description = "Attaches a file to an existing invoice using Feign client.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File successfully uploaded"),
            @ApiResponse(responseCode = "404", description = "Invoice not found"),
            @ApiResponse(responseCode = "400", description = "Failed to upload file")
    })
    @PostMapping("/attach-multipart-file/{invoiceId}")
    public ResponseEntity<Void> attachMultipartFileToInvoice(@PathVariable UUID invoiceId, @RequestParam("file") MultipartFile file) {
        invoiceFeignClient.uploadFileToInvoice(invoiceId, file);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Attach base64 file to invoice", description = "Attaches a base64 encoded file to an invoice.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File successfully uploaded"),
            @ApiResponse(responseCode = "400", description = "Failed to upload file")
    })
    @PostMapping("/attach-base64-file/{invoiceId}")
    public ResponseEntity<Void> attachBase64FileToInvoice(@PathVariable UUID invoiceId, @RequestParam("file") MultipartFile file) throws FailedToUploadFileException, ExecutionException, InterruptedException, TimeoutException {
        invoiceService.attachBase64FileToInvoice(invoiceId, file);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a new invoice", description = "Creates a new invoice record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid invoice data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO invoiceDTO) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        return ResponseEntity.ok(invoiceService.create(invoiceDTO));
        //  return invoiceFeignClient.createInvoice(invoiceDTO);
    }

    @Operation(summary = "Update an invoice", description = "Updates an existing invoice record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice updated successfully"),
            @ApiResponse(responseCode = "404", description = "Invoice not found"),
            @ApiResponse(responseCode = "400", description = "Invalid invoice data")
    })
    @PutMapping("/update")
    public ResponseEntity<InvoiceDTO> updateInvoice(@RequestBody InvoiceDTO invoiceDTO) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        return ResponseEntity.ok(invoiceService.update(invoiceDTO));
        // return invoiceFeignClient.updateInvoice(invoiceDTO);
    }

    @Operation(summary = "Delete an invoice", description = "Deletes an invoice by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Invoice deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        invoiceService.delete(id);
        // invoiceFeignClient.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all invoices", description = "Fetches a paginated list of all invoices.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoices fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public ResponseEntity<CustomPageDTO> getAllInvoices(Pageable pageable) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        return ResponseEntity.ok(invoiceService.getAll(pageable));
        //  return invoiceFeignClient.getAllInvoices(pageable);
    }

    @Operation(summary = "Get an invoice by ID", description = "Fetches an invoice by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        return ResponseEntity.ok(invoiceService.getById(id));
        //  return invoiceFeignClient.getInvoiceById(id);
    }
}
