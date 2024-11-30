package com.budgetMicroservice.controller;

import com.budgetMicroservice.dto.BudgetSubtypeDTO;
import com.budgetMicroservice.dto.CustomPageableDTO;
import com.budgetMicroservice.exception.BudgetExceededException;
import com.budgetMicroservice.exception.BudgetSubtypeAlreadyExistsException;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.BudgetTypeNotFoundException;
import com.budgetMicroservice.service.BudgetSubtypeService;
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

class BudgetSubtypeControllerTest {

    @Mock
    private BudgetSubtypeService budgetSubtypeService;

    @InjectMocks
    private BudgetSubtypeController budgetSubtypeController;

    private BudgetSubtypeDTO budgetSubtypeDTO;

    private UUID id;

    @BeforeEach
    void setUp() {
        openMocks(this);
        id = UUID.randomUUID();
        budgetSubtypeDTO = new BudgetSubtypeDTO();
        budgetSubtypeDTO.setId(UUID.randomUUID());
        budgetSubtypeDTO.setName("Test Subtype");
    }

    @Test
    void testAddSubtype_Success() throws BudgetTypeNotFoundException, BudgetSubtypeAlreadyExistsException, BudgetExceededException, BudgetSubtypeNotFoundException {
        when(budgetSubtypeService.addSubtypeToBudget(any(BudgetSubtypeDTO.class))).thenReturn(budgetSubtypeDTO);

        ResponseEntity<BudgetSubtypeDTO> response = budgetSubtypeController.addSubtype(budgetSubtypeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Subtype", response.getBody().getName());
        verify(budgetSubtypeService, times(1)).addSubtypeToBudget(any(BudgetSubtypeDTO.class));
    }

    @Test
    void testAddSubtype_BudgetTypeNotFound() throws BudgetTypeNotFoundException, BudgetSubtypeAlreadyExistsException, BudgetExceededException, BudgetSubtypeNotFoundException {
        when(budgetSubtypeService.addSubtypeToBudget(any(BudgetSubtypeDTO.class)))
                .thenThrow(new BudgetTypeNotFoundException(UUID.randomUUID()));

        BudgetTypeNotFoundException exception = assertThrows(BudgetTypeNotFoundException.class, () -> {
            budgetSubtypeController.addSubtype(budgetSubtypeDTO);
        });

        assertTrue(exception.getMessage().contains("Budget type not found"));
    }

    @Test
    void testUpdateSubtype_Success() throws BudgetSubtypeNotFoundException, BudgetSubtypeAlreadyExistsException, BudgetExceededException {
        when(budgetSubtypeService.updateBudgetSubtype(any(BudgetSubtypeDTO.class))).thenReturn(budgetSubtypeDTO);

        ResponseEntity<BudgetSubtypeDTO> response = budgetSubtypeController.updateSubtype(budgetSubtypeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(budgetSubtypeService, times(1)).updateBudgetSubtype(any(BudgetSubtypeDTO.class));
    }

    @Test
    void testUpdateSubtype_BudgetSubtypeNotFound() throws BudgetSubtypeNotFoundException, BudgetSubtypeAlreadyExistsException, BudgetExceededException {
        when(budgetSubtypeService.updateBudgetSubtype(any(BudgetSubtypeDTO.class)))
                .thenThrow(new BudgetSubtypeNotFoundException(UUID.randomUUID()));

        BudgetSubtypeNotFoundException exception = assertThrows(BudgetSubtypeNotFoundException.class, () -> {
            budgetSubtypeController.updateSubtype(budgetSubtypeDTO);
        });

        assertTrue(exception.getMessage().contains("Budget subtype not found"));
    }

    @Test
    void testDeleteSubtype_Success() throws BudgetSubtypeNotFoundException {
        UUID id = UUID.randomUUID();
        doNothing().when(budgetSubtypeService).deleteBudgetSubtype(id);

        ResponseEntity<Void> response = budgetSubtypeController.deleteSubtype(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(budgetSubtypeService, times(1)).deleteBudgetSubtype(id);
    }

    @Test
    void testDeleteSubtype_BudgetSubtypeNotFound() throws BudgetSubtypeNotFoundException {
        doThrow(new BudgetSubtypeNotFoundException(UUID.randomUUID())).when(budgetSubtypeService).deleteBudgetSubtype(id);

        BudgetSubtypeNotFoundException exception = assertThrows(BudgetSubtypeNotFoundException.class, () -> {
            budgetSubtypeController.deleteSubtype(id);
        });

        assertTrue(exception.getMessage().contains("Budget subtype not found"));
    }

    @Test
    void testFindSubtypeById_Success() throws BudgetSubtypeNotFoundException {
        when(budgetSubtypeService.findBudgetSubtypeDTOById(id)).thenReturn(budgetSubtypeDTO);

        ResponseEntity<BudgetSubtypeDTO> response = budgetSubtypeController.findSubtypeById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Subtype", response.getBody().getName());
        verify(budgetSubtypeService, times(1)).findBudgetSubtypeDTOById(id);
    }

    @Test
    void testFindSubtypeById_BudgetSubtypeNotFound() throws BudgetSubtypeNotFoundException {
        when(budgetSubtypeService.findBudgetSubtypeDTOById(id))
                .thenThrow(new BudgetSubtypeNotFoundException(UUID.randomUUID()));

        BudgetSubtypeNotFoundException exception = assertThrows(BudgetSubtypeNotFoundException.class, () -> {
            budgetSubtypeController.findSubtypeById(id);
        });

        assertTrue(exception.getMessage().contains("Budget subtype not found"));
    }

    @Test
    void testFindAllSubtypes_Success() throws Exception {
        Page<BudgetSubtypeDTO> pagedResponse = new PageImpl<>(Collections.singletonList(budgetSubtypeDTO));
        when(budgetSubtypeService.findAllBudgetSubtypes(any(CustomPageableDTO.class))).thenReturn(pagedResponse);

        ResponseEntity<Page<BudgetSubtypeDTO>> response = budgetSubtypeController.findAllSubtypes(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(budgetSubtypeService, times(1)).findAllBudgetSubtypes(any(CustomPageableDTO.class));
    }
}