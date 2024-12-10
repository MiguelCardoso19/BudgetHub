package com.portalMicroservice.controller;

import com.portalMicroservice.controller.budget.InvoiceController;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.InvoiceDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.budget.FailedToUploadFileException;
import com.portalMicroservice.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class InvoiceControllerTest {

    @InjectMocks
    private InvoiceController invoiceController;

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private MultipartFile file;

    private UUID invoiceId;

    private InvoiceDTO invoiceDTO;

    @BeforeEach
    void setUp() {
        openMocks(this);
        invoiceId = UUID.randomUUID();
        invoiceDTO = new InvoiceDTO();
    }

    @Test
    void testAttachBase64FileToInvoice_Success() throws FailedToUploadFileException, ExecutionException, InterruptedException, TimeoutException {
        doNothing().when(invoiceService).attachBase64FileToInvoice(invoiceId, file);

        ResponseEntity<Void> response = invoiceController.attachBase64FileToInvoice(invoiceId, file);

        assertEquals(204, response.getStatusCode().value());
        verify(invoiceService, times(1)).attachBase64FileToInvoice(invoiceId, file);
    }

    @Test
    void testCreateInvoice_Success() throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        when(invoiceService.create(invoiceDTO)).thenReturn(invoiceDTO);

        ResponseEntity<InvoiceDTO> response = invoiceController.createInvoice(invoiceDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(invoiceDTO, response.getBody());
        verify(invoiceService, times(1)).create(invoiceDTO);
    }

    @Test
    void testUpdateInvoice_Success() throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        when(invoiceService.update(invoiceDTO)).thenReturn(invoiceDTO);

        ResponseEntity<InvoiceDTO> response = invoiceController.updateInvoice(invoiceDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(invoiceDTO, response.getBody());
        verify(invoiceService, times(1)).update(invoiceDTO);
    }

    @Test
    void testDeleteInvoice_Success() throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        doNothing().when(invoiceService).delete(invoiceId);

        ResponseEntity<Void> response = invoiceController.deleteInvoice(invoiceId);

        assertEquals(204, response.getStatusCode().value());
        verify(invoiceService, times(1)).delete(invoiceId);
    }

    @Test
    void testGetAllInvoices_Success() throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        Pageable pageable = PageRequest.of(0, 10);

        CustomPageDTO<InvoiceDTO> customPageDTO = new CustomPageDTO<>(
                Collections.singletonList(invoiceDTO),
                pageable.getPageSize(),
                1L,
                null,
                true,
                true,
                1,
                false
        );

        when(invoiceService.getAll(pageable)).thenReturn(customPageDTO);

        ResponseEntity<CustomPageDTO> response = invoiceController.getAllInvoices(pageable);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(customPageDTO, response.getBody());
        verify(invoiceService, times(1)).getAll(pageable);
    }

    @Test
    void testGetInvoiceById_Success() throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        invoiceDTO.setId(invoiceId);
        when(invoiceService.getById(invoiceId)).thenReturn(invoiceDTO);

        ResponseEntity<InvoiceDTO> response = invoiceController.getInvoiceById(invoiceId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(invoiceDTO, response.getBody());
        verify(invoiceService, times(1)).getById(invoiceId);
    }


/* This test only works when using the Feign client
    @Test
    void testAttachMultipartFileToInvoice_Success() throws FailedToUploadFileException, ExecutionException, InterruptedException, TimeoutException {
        doNothing().when(invoiceService).attachBase64FileToInvoice(invoiceId, file);

        ResponseEntity<Void> response = invoiceController.attachMultipartFileToInvoice(invoiceId, file);

        assertEquals(204, response.getStatusCode().value());
        verify(invoiceService, times(1)).attachBase64FileToInvoice(invoiceId, file);
    }
 */
}
