package com.portalMicroservice.client;

import com.portalMicroservice.client.budget.SupplierFeignClient;
import com.portalMicroservice.dto.budget.SupplierDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class SupplierFeignClientTest {

    @Mock
    private SupplierFeignClient supplierFeignClient;

    private SupplierDTO supplierDTO;

    private UUID supplierId;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        openMocks(this);

        supplierDTO = new SupplierDTO();
        supplierDTO.setId(UUID.randomUUID());

        supplierId = UUID.randomUUID();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void createSupplier_Success() {
        when(supplierFeignClient.createSupplier(supplierDTO)).thenReturn(new ResponseEntity<>(supplierDTO, HttpStatus.CREATED));

        ResponseEntity<SupplierDTO> response = supplierFeignClient.createSupplier(supplierDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(supplierDTO);
        verify(supplierFeignClient).createSupplier(supplierDTO);
    }

    @Test
    void getSupplierById_Success() {
        when(supplierFeignClient.getSupplierById(supplierId)).thenReturn(new ResponseEntity<>(supplierDTO, HttpStatus.OK));

        ResponseEntity<SupplierDTO> response = supplierFeignClient.getSupplierById(supplierId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(supplierDTO);
        verify(supplierFeignClient).getSupplierById(supplierId);
    }

    @Test
    void updateSupplier_Success() {
        when(supplierFeignClient.updateSupplier(supplierDTO)).thenReturn(new ResponseEntity<>(supplierDTO, HttpStatus.OK));

        ResponseEntity<SupplierDTO> response = supplierFeignClient.updateSupplier(supplierDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(supplierDTO);
        verify(supplierFeignClient).updateSupplier(supplierDTO);
    }

    @Test
    void deleteSupplier_Success() {
        when(supplierFeignClient.deleteSupplier(supplierId)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        ResponseEntity<Void> response = supplierFeignClient.deleteSupplier(supplierId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(supplierFeignClient).deleteSupplier(supplierId);
    }

    @Test
    void getAllSuppliers_Success() {
        Page<SupplierDTO> page = new PageImpl<>(Collections.singletonList(supplierDTO), pageable, 1);
        when(supplierFeignClient.getAllSuppliers(pageable)).thenReturn(new ResponseEntity<>(page, HttpStatus.OK));

        ResponseEntity<Page<SupplierDTO>> response = supplierFeignClient.getAllSuppliers(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent().getFirst()).isEqualTo(supplierDTO);
        verify(supplierFeignClient).getAllSuppliers(pageable);
    }
}