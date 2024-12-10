package com.portalMicroservice.controller;

import com.portalMicroservice.controller.budget.BudgetTypeController;
import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.BudgetTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class BudgetTypeControllerTest {

    @InjectMocks
    private BudgetTypeController budgetTypeController;

    @Mock
    private BudgetTypeService budgetTypeService;

    private BudgetTypeDTO budgetTypeDTO;

    private UUID id;

    @BeforeEach
    void setUp() {
        openMocks(this);
        budgetTypeDTO = new BudgetTypeDTO();
        id = UUID.randomUUID();
    }

    @Test
    void testCreateBudgetType_Success() throws ExecutionException, InterruptedException, TimeoutException, GenericException {
        budgetTypeDTO.setName("Test Type");
        when(budgetTypeService.create(budgetTypeDTO)).thenReturn(budgetTypeDTO);

        ResponseEntity<BudgetTypeDTO> response = budgetTypeController.createBudgetType(budgetTypeDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(budgetTypeDTO, response.getBody());
        verify(budgetTypeService, times(1)).create(budgetTypeDTO);
    }

    @Test
    void testDeleteBudgetType_Success() throws ExecutionException, InterruptedException, TimeoutException {
        doNothing().when(budgetTypeService).delete(id);

        ResponseEntity<Void> response = budgetTypeController.deleteBudgetType(id);

        assertEquals(204, response.getStatusCodeValue());
        verify(budgetTypeService, times(1)).delete(id);
    }

    @Test
    void testUpdateBudgetType_Success() throws ExecutionException, InterruptedException, TimeoutException, GenericException {
        budgetTypeDTO.setName("Updated Type");
        when(budgetTypeService.update(budgetTypeDTO)).thenReturn(budgetTypeDTO);

        ResponseEntity<BudgetTypeDTO> response = budgetTypeController.updateBudgetType(budgetTypeDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(budgetTypeDTO, response.getBody());
        verify(budgetTypeService, times(1)).update(budgetTypeDTO);
    }

    @Test
    void testFindBudgetTypeById_Success() throws ExecutionException, InterruptedException, TimeoutException, GenericException {
        budgetTypeDTO.setId(id);
        budgetTypeDTO.setName("Test Type");
        when(budgetTypeService.getById(id)).thenReturn(budgetTypeDTO);

        ResponseEntity<BudgetTypeDTO> response = budgetTypeController.findBudgetTypeById(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(budgetTypeDTO, response.getBody());
        verify(budgetTypeService, times(1)).getById(id);
    }

    @Test
    void testFindAllBudgetTypes_Success() throws ExecutionException, InterruptedException, TimeoutException, GenericException {
        Pageable pageable = PageRequest.of(0, 10);
        budgetTypeDTO.setName("Test Type");

        CustomPageDTO<BudgetTypeDTO> customPageDTO = new CustomPageDTO<>(
                Collections.singletonList(budgetTypeDTO),
                pageable.getPageSize(),
                1L,
                null,
                true,
                true,
                1,
                false
        );

        when(budgetTypeService.findAll(pageable)).thenReturn(customPageDTO);

        ResponseEntity<CustomPageDTO> response = budgetTypeController.findAllBudgetTypes(pageable);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customPageDTO, response.getBody());
        verify(budgetTypeService, times(1)).findAll(pageable);
    }
}
