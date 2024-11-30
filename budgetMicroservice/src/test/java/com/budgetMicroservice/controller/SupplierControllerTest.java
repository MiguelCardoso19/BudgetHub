package com.budgetMicroservice.controller;

import com.budgetMicroservice.dto.CustomPageableDTO;
import com.budgetMicroservice.dto.SupplierDTO;
import com.budgetMicroservice.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SupplierControllerTest {

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private SupplierController supplierController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private SupplierDTO supplierDTO;

    private UUID supplierId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(supplierController).build();
        objectMapper = new ObjectMapper();
        supplierDTO = new SupplierDTO();
        supplierDTO.setCompanyName("Tech Solutions Ltd.");
        supplierDTO.setResponsibleName("John Doe");
        supplierDTO.setNif("123456789");
        supplierDTO.setPhoneNumber("+1234567890");
        supplierDTO.setEmail("supplier@example.com");

        supplierId = UUID.randomUUID();
    }

    @Test
    void createSupplierTest_Success() throws Exception {
        when(supplierService.create(any(SupplierDTO.class))).thenReturn(supplierDTO);

        mockMvc.perform(post("/api/v1/supplier/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplierDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("Tech Solutions Ltd."))
                .andExpect(jsonPath("$.responsibleName").value("John Doe"))
                .andExpect(jsonPath("$.nif").value("123456789"));
    }

    @Test
    void getSupplierByIdTest_Success() throws Exception {
        UUID supplierId = UUID.randomUUID();
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCompanyName("Tech Solutions Ltd.");
        supplierDTO.setResponsibleName("John Doe");
        supplierDTO.setNif("123456789");

        when(supplierService.findSupplierDTOById(supplierId)).thenReturn(supplierDTO);

        mockMvc.perform(get("/api/v1/supplier/{id}", supplierId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("Tech Solutions Ltd."))
                .andExpect(jsonPath("$.responsibleName").value("John Doe"))
                .andExpect(jsonPath("$.nif").value("123456789"));
    }

    @Test
    void updateSupplierTest_Success() throws Exception {
        when(supplierService.update(any(SupplierDTO.class))).thenReturn(supplierDTO);

        mockMvc.perform(put("/api/v1/supplier/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplierDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("Tech Solutions Ltd."))
                .andExpect(jsonPath("$.responsibleName").value("John Doe"))
                .andExpect(jsonPath("$.nif").value("123456789"));
    }

    @Test
    void deleteSupplierTest_Success() throws Exception {
        doNothing().when(supplierService).delete(supplierId);

        mockMvc.perform(delete("/api/v1/supplier/{id}", supplierId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllSuppliersTest_Success() throws Exception {
        Page<SupplierDTO> supplierPage = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 2);

        when(supplierService.findAll(any(CustomPageableDTO.class))).thenReturn(supplierPage);

        ResponseEntity<Page<SupplierDTO>> response = supplierController.findAll(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
        verify(supplierService, times(1)).findAll(any(CustomPageableDTO.class));
    }
}