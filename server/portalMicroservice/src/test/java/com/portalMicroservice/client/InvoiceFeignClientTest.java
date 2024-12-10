package com.portalMicroservice.client;

import com.portalMicroservice.client.budget.InvoiceFeignClient;
import com.portalMicroservice.dto.budget.InvoiceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class InvoiceFeignClientTest {

    @Mock
    private InvoiceFeignClient invoiceFeignClient;

    private InvoiceDTO invoiceDTO;

    private UUID invoiceId;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        openMocks(this);

        invoiceDTO = new InvoiceDTO();
        invoiceDTO.setId(UUID.randomUUID());
        invoiceDTO.setDescription("Test Invoice");

        invoiceId = UUID.randomUUID();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void uploadFileToInvoice_Success() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "This is a test file".getBytes()
        );

        when(invoiceFeignClient.uploadFileToInvoice(invoiceId, mockFile)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<Void> response = invoiceFeignClient.uploadFileToInvoice(invoiceId, mockFile);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(invoiceFeignClient).uploadFileToInvoice(invoiceId, mockFile);
    }

    @Test
    void createInvoice_Success() {
        when(invoiceFeignClient.createInvoice(invoiceDTO)).thenReturn(new ResponseEntity<>(invoiceDTO, HttpStatus.CREATED));

        ResponseEntity<InvoiceDTO> response = invoiceFeignClient.createInvoice(invoiceDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(invoiceDTO);
        verify(invoiceFeignClient).createInvoice(invoiceDTO);
    }

    @Test
    void updateInvoice_Success() {
        when(invoiceFeignClient.updateInvoice(invoiceDTO)).thenReturn(new ResponseEntity<>(invoiceDTO, HttpStatus.OK));

        ResponseEntity<InvoiceDTO> response = invoiceFeignClient.updateInvoice(invoiceDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(invoiceDTO);
        verify(invoiceFeignClient).updateInvoice(invoiceDTO);
    }

    @Test
    void deleteInvoice_Success() {
        when(invoiceFeignClient.deleteInvoice(invoiceId)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        ResponseEntity<Void> response = invoiceFeignClient.deleteInvoice(invoiceId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(invoiceFeignClient).deleteInvoice(invoiceId);
    }

    @Test
    void getAllInvoices_Success() {
        Page<InvoiceDTO> page = new PageImpl<>(Collections.singletonList(invoiceDTO), pageable, 1);
        when(invoiceFeignClient.getAllInvoices(pageable)).thenReturn(new ResponseEntity<>(page, HttpStatus.OK));

        ResponseEntity<Page<InvoiceDTO>> response = invoiceFeignClient.getAllInvoices(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent().get(0)).isEqualTo(invoiceDTO);
        verify(invoiceFeignClient).getAllInvoices(pageable);
    }

    @Test
    void getInvoiceById_Success() {
        when(invoiceFeignClient.getInvoiceById(invoiceId)).thenReturn(new ResponseEntity<>(invoiceDTO, HttpStatus.OK));

        ResponseEntity<InvoiceDTO> response = invoiceFeignClient.getInvoiceById(invoiceId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(invoiceDTO);
        verify(invoiceFeignClient).getInvoiceById(invoiceId);
    }
}
