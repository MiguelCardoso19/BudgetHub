package com.portalMicroservice.client;

import com.portalMicroservice.client.budget.MovementFeignClient;
import com.portalMicroservice.dto.budget.MovementDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static com.portalMicroservice.enumerator.MovementStatus.SUCCEEDED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class MovementFeignClientTest {

    @Mock
    private MovementFeignClient movementFeignClient;

    private MovementDTO movementDTO;

    private UUID movementId;

    private UUID budgetTypeId;

    private UUID budgetSubtypeId;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        openMocks(this);

        movementDTO = new MovementDTO();
        movementDTO.setId(UUID.randomUUID());
        movementDTO.setDescription("Test Movement");

        movementId = UUID.randomUUID();
        budgetTypeId = UUID.randomUUID();
        budgetSubtypeId = UUID.randomUUID();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void createMovement_Success() {
        when(movementFeignClient.createMovement(movementDTO)).thenReturn(new ResponseEntity<>(movementDTO, HttpStatus.CREATED));

        ResponseEntity<MovementDTO> response = movementFeignClient.createMovement(movementDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(movementDTO);
        verify(movementFeignClient).createMovement(movementDTO);
    }

    @Test
    void updateMovement_Success() {
        when(movementFeignClient.updateMovement(movementDTO)).thenReturn(new ResponseEntity<>(movementDTO, HttpStatus.OK));

        ResponseEntity<MovementDTO> response = movementFeignClient.updateMovement(movementDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(movementDTO);
        verify(movementFeignClient).updateMovement(movementDTO);
    }

    @Test
    void getMovementById_Success() {
        when(movementFeignClient.getMovementById(movementId)).thenReturn(new ResponseEntity<>(movementDTO, HttpStatus.OK));

        ResponseEntity<MovementDTO> response = movementFeignClient.getMovementById(movementId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(movementDTO);
        verify(movementFeignClient).getMovementById(movementId);
    }

    @Test
    void deleteMovement_Success() {
        when(movementFeignClient.deleteMovement(movementId)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        ResponseEntity<Void> response = movementFeignClient.deleteMovement(movementId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(movementFeignClient).deleteMovement(movementId);
    }

    @Test
    void getAllMovements_Success() {
        Page<MovementDTO> page = new PageImpl<>(Collections.singletonList(movementDTO), pageable, 1);
        when(movementFeignClient.getAllMovements(pageable)).thenReturn(new ResponseEntity<>(page, HttpStatus.OK));

        ResponseEntity<Page<MovementDTO>> response = movementFeignClient.getAllMovements(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent().get(0)).isEqualTo(movementDTO);
        verify(movementFeignClient).getAllMovements(pageable);
    }

    @Test
    void getMovementsByBudgetType_Success() {
        Page<MovementDTO> page = new PageImpl<>(Collections.singletonList(movementDTO), pageable, 1);
        when(movementFeignClient.getMovementsByBudgetType(budgetTypeId, pageable)).thenReturn(new ResponseEntity<>(page, HttpStatus.OK));

        ResponseEntity<Page<MovementDTO>> response = movementFeignClient.getMovementsByBudgetType(budgetTypeId, pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent().get(0)).isEqualTo(movementDTO);
        verify(movementFeignClient).getMovementsByBudgetType(budgetTypeId, pageable);
    }

    @Test
    void getMovementsByBudgetSubtype_Success() {
        Page<MovementDTO> page = new PageImpl<>(Collections.singletonList(movementDTO), pageable, 1);
        when(movementFeignClient.getMovementsByBudgetSubtype(budgetSubtypeId, pageable)).thenReturn(new ResponseEntity<>(page, HttpStatus.OK));

        ResponseEntity<Page<MovementDTO>> response = movementFeignClient.getMovementsByBudgetSubtype(budgetSubtypeId, pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent().get(0)).isEqualTo(movementDTO);
        verify(movementFeignClient).getMovementsByBudgetSubtype(budgetSubtypeId, pageable);
    }

    @Test
    void exportMovementsReport_Success() {
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
        String userEmail = "test@example.com";

        when(movementFeignClient.exportMovementsReport(startDate, endDate, SUCCEEDED, userEmail)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<Void> response = movementFeignClient.exportMovementsReport(startDate, endDate, SUCCEEDED, userEmail);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(movementFeignClient).exportMovementsReport(startDate, endDate, SUCCEEDED, userEmail);
    }
}
