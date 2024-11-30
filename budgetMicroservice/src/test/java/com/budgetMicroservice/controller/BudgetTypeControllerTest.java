package com.budgetMicroservice.controller;

import com.budgetMicroservice.dto.BudgetTypeDTO;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.service.BudgetTypeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

class BudgetTypeControllerTest {

    @Mock
    private BudgetTypeService budgetTypeService;

    @InjectMocks
    private BudgetTypeController budgetTypeController;

    private BudgetTypeDTO budgetTypeDTO;

    private UUID id;

    @BeforeEach
    void setUp() {
        openMocks(this);
        id = UUID.randomUUID();
        budgetTypeDTO = new BudgetTypeDTO();
        budgetTypeDTO.setId(UUID.randomUUID());
        budgetTypeDTO.setName("Test Budget Type");
    }

    @Test
    void testCreateBudgetType_Success() throws BudgetTypeAlreadyExistsException {
        when(budgetTypeService.createBudgetType(any(BudgetTypeDTO.class))).thenReturn(budgetTypeDTO);

        ResponseEntity<BudgetTypeDTO> response = budgetTypeController.createBudgetType(budgetTypeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Budget Type", response.getBody().getName());
        verify(budgetTypeService, times(1)).createBudgetType(any(BudgetTypeDTO.class));
    }

    @Test
    void testCreateBudgetType_Conflict() throws BudgetTypeAlreadyExistsException {
        String expectedMessage = "Budget type already exists";
        when(budgetTypeService.createBudgetType(any(BudgetTypeDTO.class)))
                .thenThrow(new BudgetTypeAlreadyExistsException(expectedMessage));

        BudgetTypeAlreadyExistsException exception = assertThrows(BudgetTypeAlreadyExistsException.class, () -> {
            budgetTypeController.createBudgetType(budgetTypeDTO);
        });

        assertTrue(exception.getMessage().contains(expectedMessage),
                "Exception message should contain: " + expectedMessage);
    }

    @Test
    void testDeleteBudgetType_Success() throws BudgetTypeNotFoundException {
        doNothing().when(budgetTypeService).deleteBudgetType(id);

        ResponseEntity<Void> response = budgetTypeController.deleteBudgetType(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(budgetTypeService, times(1)).deleteBudgetType(id);
    }

    @Test
    void testDeleteBudgetType_NotFound() throws BudgetTypeNotFoundException {
        doThrow(new BudgetTypeNotFoundException(UUID.randomUUID())).when(budgetTypeService).deleteBudgetType(id);

        BudgetTypeNotFoundException exception = assertThrows(BudgetTypeNotFoundException.class, () -> {
            budgetTypeController.deleteBudgetType(id);
        });

        assertTrue(exception.getMessage().contains("Budget type not found"));
    }

    @Test
    void testUpdateBudgetType_Success() throws BudgetTypeNotFoundException, BudgetTypeAlreadyExistsException, BudgetSubtypeNotFoundException {
        when(budgetTypeService.updateBudgetType(any(BudgetTypeDTO.class))).thenReturn(budgetTypeDTO);

        ResponseEntity<BudgetTypeDTO> response = budgetTypeController.updateBudgetType(budgetTypeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Budget Type", response.getBody().getName());
        verify(budgetTypeService, times(1)).updateBudgetType(any(BudgetTypeDTO.class));
    }

    @Test
    void testUpdateBudgetType_NotFound() throws BudgetTypeNotFoundException, BudgetTypeAlreadyExistsException, BudgetSubtypeNotFoundException {
        when(budgetTypeService.updateBudgetType(any(BudgetTypeDTO.class)))
                .thenThrow(new BudgetTypeNotFoundException(UUID.randomUUID()));

        BudgetTypeNotFoundException exception = assertThrows(BudgetTypeNotFoundException.class, () -> {
            budgetTypeController.updateBudgetType(budgetTypeDTO);
        });

        assertTrue(exception.getMessage().contains("Budget type not found"));
    }

    @Test
    void testFindBudgetTypeById_Success() throws BudgetTypeNotFoundException, BudgetSubtypeNotFoundException {
        when(budgetTypeService.findBudgetTypeDTOById(id)).thenReturn(budgetTypeDTO);

        ResponseEntity<BudgetTypeDTO> response = budgetTypeController.findBudgetTypeById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Budget Type", response.getBody().getName());
        verify(budgetTypeService, times(1)).findBudgetTypeDTOById(id);
    }

    @Test
    void testFindBudgetTypeById_NotFound() throws BudgetTypeNotFoundException, BudgetSubtypeNotFoundException {
        when(budgetTypeService.findBudgetTypeDTOById(id))
                .thenThrow(new BudgetTypeNotFoundException(UUID.randomUUID()));

        BudgetTypeNotFoundException exception = assertThrows(BudgetTypeNotFoundException.class, () -> {
            budgetTypeController.findBudgetTypeById(id);
        });

        assertTrue(exception.getMessage().contains("Budget type not found"));
    }

    @Test
    void testFindAllBudgetTypes_Success() throws JsonProcessingException {
        Page<BudgetTypeDTO> pagedResponse = new PageImpl<>(Collections.singletonList(budgetTypeDTO));
        when(budgetTypeService.findAllBudgetTypes(any())).thenReturn(pagedResponse);

        ResponseEntity<Page<BudgetTypeDTO>> response = budgetTypeController.findAllBudgetTypes(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(budgetTypeService, times(1)).findAllBudgetTypes(any());
    }
}
