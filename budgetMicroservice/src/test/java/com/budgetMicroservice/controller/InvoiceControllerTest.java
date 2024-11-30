package com.budgetMicroservice.controller;

import com.budgetMicroservice.dto.InvoiceDTO;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

class InvoiceControllerTest {

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    private InvoiceDTO invoiceDTO;

    private UUID invoiceId;

    @BeforeEach
    void setUp() {
        openMocks(this);
        invoiceId = UUID.randomUUID();
        invoiceDTO = new InvoiceDTO();
        invoiceDTO.setId(invoiceId);
        invoiceDTO.setMovementDocumentNumber("INV-123");
    }

    @Test
    void testCreateInvoice_Success() throws InvoiceAlreadyExistsException, MovementNotFoundException, DocumentNumberNotFoundException {
        when(invoiceService.create(any(InvoiceDTO.class))).thenReturn(invoiceDTO);

        ResponseEntity<InvoiceDTO> response = invoiceController.createInvoice(invoiceDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INV-123", response.getBody().getMovementDocumentNumber());
        verify(invoiceService, times(1)).create(any(InvoiceDTO.class));
    }

    @Test
    void testCreateInvoice_Conflict() throws InvoiceAlreadyExistsException, MovementNotFoundException, DocumentNumberNotFoundException {
        when(invoiceService.create(any(InvoiceDTO.class)))
                .thenThrow(new InvoiceAlreadyExistsException("fakedata"));

        InvoiceAlreadyExistsException exception = assertThrows(InvoiceAlreadyExistsException.class, () -> {
            invoiceController.createInvoice(invoiceDTO);
        });

        assertEquals("A invoice with the document number 'fakedata' already exists", exception.getMessage());
    }

    @Test
    void testUpdateInvoice_Success() throws InvoiceNotFoundException, InvoiceAlreadyExistsException {
        when(invoiceService.update(any(InvoiceDTO.class))).thenReturn(invoiceDTO);

        ResponseEntity<InvoiceDTO> response = invoiceController.updateInvoice(invoiceDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(invoiceService, times(1)).update(any(InvoiceDTO.class));
    }

    @Test
    void testUpdateInvoice_NotFound() throws InvoiceNotFoundException, InvoiceAlreadyExistsException {
        when(invoiceService.update(any(InvoiceDTO.class)))
                .thenThrow(new InvoiceNotFoundException(UUID.randomUUID()));

        InvoiceNotFoundException exception = assertThrows(InvoiceNotFoundException.class, () -> {
            invoiceController.updateInvoice(invoiceDTO);
        });

        assertTrue(exception.getMessage().contains("Invoice not found"));
    }

    @Test
    void testDeleteInvoice_Success() throws InvoiceNotFoundException {
        doNothing().when(invoiceService).delete(invoiceId);

        ResponseEntity<Void> response = invoiceController.deleteInvoice(invoiceId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(invoiceService, times(1)).delete(invoiceId);
    }

    @Test
    void testDeleteInvoice_NotFound() throws InvoiceNotFoundException {
        doThrow(new InvoiceNotFoundException(UUID.randomUUID())).when(invoiceService).delete(invoiceId);

        InvoiceNotFoundException exception = assertThrows(InvoiceNotFoundException.class, () -> {
            invoiceController.deleteInvoice(invoiceId);
        });

        assertTrue(exception.getMessage().contains("Invoice not found"));
    }

    @Test
    void testGetAllInvoices_Success() throws IOException {
        Page<InvoiceDTO> invoicePage = mock(Page.class);
        when(invoiceService.getAll(any())).thenReturn(invoicePage);

        ResponseEntity<Page<InvoiceDTO>> response = invoiceController.getAllInvoices(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(invoiceService, times(1)).getAll(any());
    }

    @Test
    void testGetById_Success() throws InvoiceNotFoundException, IOException {
        when(invoiceService.findInvoiceDTOById(invoiceId)).thenReturn(invoiceDTO);

        ResponseEntity<InvoiceDTO> response = invoiceController.getById(invoiceId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INV-123", response.getBody().getMovementDocumentNumber());
        verify(invoiceService, times(1)).findInvoiceDTOById(invoiceId);
    }

    @Test
    void testGetById_NotFound() throws InvoiceNotFoundException, IOException {
        when(invoiceService.findInvoiceDTOById(invoiceId))
                .thenThrow(new InvoiceNotFoundException(UUID.randomUUID()));

        InvoiceNotFoundException exception = assertThrows(InvoiceNotFoundException.class, () -> {
            invoiceController.getById(invoiceId);
        });

        assertTrue(exception.getMessage().contains("Invoice not found"));
    }

    @Test
    void testUploadFileToInvoice_Success() throws InvoiceNotFoundException, FailedToUploadFileException {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());
        doNothing().when(invoiceService).attachMultipartFileToInvoice(any(UUID.class), any(MultipartFile.class));

        ResponseEntity<InvoiceDTO> response = invoiceController.uploadFileToInvoice(invoiceId, file);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(invoiceService, times(1)).attachMultipartFileToInvoice(any(UUID.class), any(MultipartFile.class));
    }

    @Test
    void testUploadFileToInvoice_InvoiceNotFound() throws InvoiceNotFoundException, FailedToUploadFileException {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());
        doThrow(new InvoiceNotFoundException(UUID.randomUUID())).when(invoiceService).attachMultipartFileToInvoice(any(UUID.class), any(MultipartFile.class));

        InvoiceNotFoundException exception = assertThrows(InvoiceNotFoundException.class, () -> {
            invoiceController.uploadFileToInvoice(invoiceId, file);
        });

        assertTrue(exception.getMessage().contains("Invoice not found"));
    }
}