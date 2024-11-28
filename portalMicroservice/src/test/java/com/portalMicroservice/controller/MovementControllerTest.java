package com.portalMicroservice.controller;

import com.portalMicroservice.controller.budget.MovementController;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.MovementDTO;
import com.portalMicroservice.enumerator.MovementStatus;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.MovementService;
import com.portalMicroservice.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MovementControllerTest {

    @Mock
    private MovementService movementService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private MovementController movementController;

    private MovementDTO movementDTO;

    private UUID id;

    private CustomPageDTO customPageDTO;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        movementDTO = new MovementDTO();
        movementDTO.setId(id);
        customPageDTO = new CustomPageDTO();
    }

    @Test
    void testCreateMovement_Success() throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        when(movementService.create(movementDTO)).thenReturn(movementDTO);

        ResponseEntity<MovementDTO> response = movementController.create(movementDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movementDTO, response.getBody());
        verify(movementService, times(1)).create(movementDTO);
    }

    @Test
    void testUpdateMovement_Success() throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        when(movementService.update(movementDTO)).thenReturn(movementDTO);

        ResponseEntity<MovementDTO> response = movementController.update(movementDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movementDTO, response.getBody());
        verify(movementService, times(1)).update(movementDTO);
    }

    @Test
    void testGetMovementById_Success() throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        when(movementService.getById(id)).thenReturn(movementDTO);

        ResponseEntity<MovementDTO> response = movementController.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movementDTO, response.getBody());
        verify(movementService, times(1)).getById(id);
    }

    @Test
    void testDeleteMovement_Success() throws ExecutionException, InterruptedException, TimeoutException {
        doNothing().when(movementService).delete(id);

        ResponseEntity<Void> response = movementController.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(movementService, times(1)).delete(id);
    }

    @Test
    void testGetAllMovements_Success() throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        when(movementService.getAll(PageRequest.of(0, 10))).thenReturn(customPageDTO);

        ResponseEntity<CustomPageDTO> response = movementController.getAll(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customPageDTO, response.getBody());
        verify(movementService, times(1)).getAll(PageRequest.of(0, 10));
    }

    @Test
    void testGetAllMovements_EmptyList() throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        when(movementService.getAll(PageRequest.of(0, 10))).thenReturn(customPageDTO);

        ResponseEntity<CustomPageDTO> response = movementController.getAll(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customPageDTO, response.getBody());
    }

    @Test
    void testGetMovementsByBudgetType_Success() throws ExecutionException, InterruptedException, TimeoutException {
        when(movementService.getByBudgetType(id, PageRequest.of(0, 10))).thenReturn(customPageDTO);

        ResponseEntity<CustomPageDTO> response = movementController.getByBudgetType(id, PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customPageDTO, response.getBody());
        verify(movementService, times(1)).getByBudgetType(id, PageRequest.of(0, 10));
    }

    @Test
    void testGetMovementsByBudgetSubtype_Success() throws ExecutionException, InterruptedException, TimeoutException {
        when(movementService.getByBudgetSubtype(id, PageRequest.of(0, 10))).thenReturn(customPageDTO);

        ResponseEntity<CustomPageDTO> response = movementController.getByBudgetSubtype(id, PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customPageDTO, response.getBody());
        verify(movementService, times(1)).getByBudgetSubtype(id, PageRequest.of(0, 10));
    }

    @Test
    void testExportMovementsReport_Success() throws ExecutionException, InterruptedException, TimeoutException {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        MovementStatus status = MovementStatus.ACCEPTED;
        when(jwtService.getEmailFromRequest()).thenReturn("test@example.com");

        ResponseEntity<Void> response = movementController.exportMovementsReport(startDate, endDate, status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(movementService, times(1)).exportMovementsReport(startDate, endDate, status, "test@example.com");
    }
}
