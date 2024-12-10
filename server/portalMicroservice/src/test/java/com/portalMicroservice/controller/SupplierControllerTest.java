package com.portalMicroservice.controller;

import com.portalMicroservice.controller.budget.SupplierController;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.SupplierDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.budget.SupplierNotFoundException;
import com.portalMicroservice.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SupplierControllerTest {

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private SupplierController supplierController;

    private SupplierDTO supplierDTO;

    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        supplierDTO = new SupplierDTO();
        supplierDTO.setId(id);
        supplierDTO.setEmail("test@example.com");
    }

    @Test
    void testCreateSupplier_Success() throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        when(supplierService.create(supplierDTO)).thenReturn(supplierDTO);

        ResponseEntity<SupplierDTO> response = supplierController.create(supplierDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(supplierDTO, response.getBody());
        verify(supplierService, times(1)).create(supplierDTO);
    }

    @Test
    void testUpdateSupplier_Success() throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        when(supplierService.update(supplierDTO)).thenReturn(supplierDTO);

        ResponseEntity<SupplierDTO> response = supplierController.update(supplierDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(supplierDTO, response.getBody());
        verify(supplierService, times(1)).update(supplierDTO);
    }

    @Test
    void testDeleteSupplier_Success() throws ExecutionException, InterruptedException, TimeoutException {
        doNothing().when(supplierService).delete(id);

        ResponseEntity<Void> response = supplierController.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(supplierService, times(1)).delete(id);
    }

    @Test
    void testGetSupplierById_Success() throws GenericException, ExecutionException, InterruptedException, TimeoutException, SupplierNotFoundException {
        when(supplierService.getById(id)).thenReturn(supplierDTO);

        ResponseEntity<SupplierDTO> response = supplierController.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(supplierDTO, response.getBody());
        verify(supplierService, times(1)).getById(id);
    }

    @Test
    void testGetAllSuppliers_Success() throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        CustomPageDTO customPageDTO = new CustomPageDTO();
        when(supplierService.getAll(PageRequest.of(0, 10))).thenReturn(customPageDTO);

        ResponseEntity<CustomPageDTO> response = supplierController.getAll(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customPageDTO, response.getBody());
        verify(supplierService, times(1)).getAll(PageRequest.of(0, 10));
    }

}
