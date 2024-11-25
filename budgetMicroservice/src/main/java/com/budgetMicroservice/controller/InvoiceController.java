package com.budgetMicroservice.controller;

import com.budgetMicroservice.dto.InvoiceDTO;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.service.InvoiceService;
import com.budgetMicroservice.util.PageableUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoice")
@RequiredArgsConstructor
@Tag(name = "Invoice Controller", description = "Operations related to Invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @Operation(summary = "Upload a file to an invoice",
            description = "Attach a multipart file to an existing invoice.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File successfully uploaded"),
            @ApiResponse(responseCode = "404", description = "Invoice not found"),
            @ApiResponse(responseCode = "400", description = "Failed to upload the file")
    })
    @PostMapping("/attach-multipart-file/{invoiceId}")
    public ResponseEntity<InvoiceDTO> uploadFileToInvoice(
            @PathVariable UUID invoiceId,
            @RequestParam("file") MultipartFile file) throws InvoiceNotFoundException, FailedToUploadFileException {
        invoiceService.attachMultipartFileToInvoice(invoiceId, file);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a new invoice",
            description = "Creates a new invoice linked to a movement, if applicable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a new invoice"),
            @ApiResponse(responseCode = "404", description = "Movement not found"),
            @ApiResponse(responseCode = "409", description = "Invoice already exists")
    })
    @PostMapping("/create")
    public ResponseEntity<InvoiceDTO> createInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO) throws InvoiceAlreadyExistsException, MovementNotFoundException, DocumentNumberNotFoundException {
        return ResponseEntity.ok(invoiceService.create(invoiceDTO));
    }

    @Operation(summary = "Update an existing invoice",
            description = "Updates the details of an existing invoice.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the invoice"),
            @ApiResponse(responseCode = "404", description = "Invoice not found"),
            @ApiResponse(responseCode = "409", description = "Invoice already exists")
    })
    @PutMapping("/update")
    public ResponseEntity<InvoiceDTO> updateInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO)
            throws InvoiceNotFoundException, InvoiceAlreadyExistsException {
        return ResponseEntity.ok(invoiceService.update(invoiceDTO));
    }

    @Operation(summary = "Delete an invoice",
            description = "Deletes an invoice by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the invoice"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable UUID id) throws InvoiceNotFoundException {
        invoiceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all invoices",
            description = "Fetches a paginated list of all invoices.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the list of invoices"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public ResponseEntity<Page<InvoiceDTO>> getAllInvoices(@PageableDefault(size = 10, page = 0) Pageable pageable) throws JsonProcessingException {
        return ResponseEntity.ok(invoiceService.getAll(PageableUtils.convertToCustomPageable(pageable)));
    }

    @Operation(summary = "Get an invoice by ID",
            description = "Fetches an invoice by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the invoice"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getById(@PathVariable UUID id) throws InvoiceNotFoundException, IOException {
        return ResponseEntity.ok(invoiceService.findInvoiceDTOById(id));
    }
}