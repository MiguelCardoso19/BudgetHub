package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.CustomPageableDTO;
import com.budgetMicroservice.dto.SupplierDTO;
import com.budgetMicroservice.exception.SupplierNotFoundException;
import com.budgetMicroservice.exception.SupplierValidationException;
import com.budgetMicroservice.mapper.SupplierMapper;
import com.budgetMicroservice.model.Supplier;
import com.budgetMicroservice.repository.SupplierRepository;
import com.budgetMicroservice.service.impl.SupplierServiceImpl;
import com.budgetMicroservice.util.PageableUtils;
import com.budgetMicroservice.validator.SupplierValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierMapper supplierMapper;

    @Mock
    private SupplierValidator supplierValidator;

    @Mock
    private KafkaTemplate<String, SupplierDTO> kafkaSupplierTemplate;

    @Mock
    private KafkaTemplate<String, UUID> kafkaUuidTemplate;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private Supplier supplier;

    private SupplierDTO supplierDTO;

    private UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        openMocks(this);
        UUID id = UUID.randomUUID();
        supplier = new Supplier();
        supplier.setId(id);

        supplierDTO = new SupplierDTO();
        supplierDTO.setId(id);
    }

    @Test
    void testCreateSupplier_Success() throws SupplierValidationException {
        when(supplierMapper.toEntity(supplierDTO)).thenReturn(supplier);
        when(supplierRepository.save(supplier)).thenReturn(supplier);
        when(supplierMapper.toDTO(supplier)).thenReturn(supplierDTO);

        SupplierDTO result = supplierService.create(supplierDTO);

        assertNotNull(result);
        assertEquals(supplierDTO.getId(), result.getId());
        verify(supplierValidator, times(1)).validateSupplierCreation(eq(supplierDTO), any(SupplierRepository.class));
    }

    @Test
    void testUpdateSupplier_Success() throws SupplierValidationException, SupplierNotFoundException {
        when(supplierRepository.findById(supplierDTO.getId())).thenReturn(Optional.of(supplier));
        when(supplierMapper.toEntity(supplierDTO)).thenReturn(supplier);
        when(supplierRepository.save(supplier)).thenReturn(supplier);
        when(supplierMapper.toDTO(supplier)).thenReturn(supplierDTO);

        SupplierDTO result = supplierService.update(supplierDTO);

        assertNotNull(result);
        assertEquals(supplierDTO.getId(), result.getId());
        verify(supplierValidator, times(1)).validateSupplierUpdate(eq(supplierDTO), any(SupplierRepository.class));
        verify(kafkaSupplierTemplate, times(1)).send(eq("supplier-response"), eq(supplierDTO));
    }

    @Test
    void testDeleteSupplier_Success() throws SupplierNotFoundException {
        when(supplierRepository.existsById(supplier.getId())).thenReturn(true);
        when(supplierRepository.findById(supplier.getId())).thenReturn(Optional.of(supplier));

        supplierService.delete(supplier.getId());

        verify(supplierRepository, times(1)).delete(eq(supplier));
    }

    @Test
    void testDeleteSupplier_NotFound() {
        when(supplierRepository.existsById(id)).thenReturn(false);

        assertThrows(SupplierNotFoundException.class, () -> supplierService.delete(id));
        verify(kafkaUuidTemplate, never()).send(eq("supplier-delete-success-response"), any(UUID.class));
    }

    @Test
    void testFindAllSuppliers_Success() {
        CustomPageableDTO pageableDTO = new CustomPageableDTO(UUID.randomUUID(), 1, 10, 0, true, false, null);
        Pageable pageable = PageableUtils.convertToPageable(pageableDTO);

        List<Supplier> suppliers = List.of(supplier);

        when(supplierRepository.findAll(pageable)).thenReturn(new PageImpl<>(suppliers, pageable, suppliers.size()));

        Page<SupplierDTO> result = supplierService.findAll(pageableDTO);

        assertNotNull(result);
        assertEquals(11, result.getTotalElements());
    }

    @Test
    void testFindSupplierById_Success() throws SupplierNotFoundException {
        when(supplierRepository.findById(supplier.getId())).thenReturn(Optional.of(supplier));
        when(supplierMapper.toDTO(supplier)).thenReturn(supplierDTO);

        SupplierDTO result = supplierService.findSupplierDTOById(supplier.getId());

        assertNotNull(result);
        assertEquals(supplier.getId(), result.getId());
    }

    @Test
    void testFindSupplierById_NotFound() {
        when(supplierRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(SupplierNotFoundException.class, () -> supplierService.findSupplierDTOById(id));
    }
}
