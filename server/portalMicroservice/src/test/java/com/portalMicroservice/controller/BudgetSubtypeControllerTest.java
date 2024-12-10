package com.portalMicroservice.controller;

import com.portalMicroservice.controller.budget.BudgetSubtypeController;
import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.BudgetSubtypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
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
    }

    @Test
    void testAddSubtype_Success() throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        budgetSubtypeDTO.setName("Test Subtype");
        when(budgetSubtypeService.addSubtypeToBudget(budgetSubtypeDTO)).thenReturn(budgetSubtypeDTO);

        ResponseEntity<BudgetSubtypeDTO> response = budgetSubtypeController.addSubtype(budgetSubtypeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budgetSubtypeDTO, response.getBody());
        verify(budgetSubtypeService, times(1)).addSubtypeToBudget(budgetSubtypeDTO);
    }

    @Test
    void testUpdateSubtype_Success() throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        budgetSubtypeDTO.setName("Updated Subtype");
        when(budgetSubtypeService.update(budgetSubtypeDTO)).thenReturn(budgetSubtypeDTO);

        ResponseEntity<BudgetSubtypeDTO> response = budgetSubtypeController.updateSubtype(budgetSubtypeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budgetSubtypeDTO, response.getBody());
        verify(budgetSubtypeService, times(1)).update(budgetSubtypeDTO);
    }

    @Test
    void testDeleteSubtype_Success() throws ExecutionException, InterruptedException, TimeoutException {
        doNothing().when(budgetSubtypeService).delete(id);

        ResponseEntity<Void> response = budgetSubtypeController.deleteSubtype(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(budgetSubtypeService, times(1)).delete(id);
    }

    @Test
    void testFindSubtypeById_Success() throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        budgetSubtypeDTO.setId(id);
        when(budgetSubtypeService.getById(id)).thenReturn(budgetSubtypeDTO);

        ResponseEntity<BudgetSubtypeDTO> response = budgetSubtypeController.findSubtypeById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budgetSubtypeDTO, response.getBody());
        verify(budgetSubtypeService, times(1)).getById(id);
    }

    @Test
    void testFindAllSubtypes_Success() throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        Pageable pageable = PageRequest.of(0, 10);
        budgetSubtypeDTO.setName("Test Subtype");

        List<BudgetSubtypeDTO> content = Collections.singletonList(budgetSubtypeDTO);
        CustomPageableDTO customPageable = new CustomPageableDTO();
        customPageable.setPageNumber(pageable.getPageNumber());
        customPageable.setPageSize(pageable.getPageSize());

        CustomPageDTO<BudgetSubtypeDTO> customPageDTO = new CustomPageDTO<>(
                content,
                pageable.getPageSize(),
                1L,
                customPageable,
                true,
                true,
                content.size(),
                content.isEmpty()
        );

        when(budgetSubtypeService.findAll(pageable)).thenReturn(customPageDTO);

        ResponseEntity<CustomPageDTO> response = budgetSubtypeController.findAllSubtypes(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customPageDTO, response.getBody());
        verify(budgetSubtypeService, times(1)).findAll(pageable);
    }
}
