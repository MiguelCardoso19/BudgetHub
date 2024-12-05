package com.portalMicroservice.client;

import com.portalMicroservice.client.budget.BudgetSubtypeFeignClient;
import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
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

class BudgetSubtypeFeignClientTest {

    @Mock
    private BudgetSubtypeFeignClient budgetSubtypeFeignClient;

    private BudgetSubtypeDTO budgetSubtypeDTO;

    private UUID id;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        openMocks(this);

        budgetSubtypeDTO = new BudgetSubtypeDTO();
        budgetSubtypeDTO.setId(UUID.randomUUID());
        budgetSubtypeDTO.setName("Test Subtype");

        id = UUID.randomUUID();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void addSubtype_Success() {
        when(budgetSubtypeFeignClient.addSubtype(budgetSubtypeDTO)).thenReturn(new ResponseEntity<>(budgetSubtypeDTO, HttpStatus.CREATED));

        ResponseEntity<BudgetSubtypeDTO> response = budgetSubtypeFeignClient.addSubtype(budgetSubtypeDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(budgetSubtypeDTO);
        verify(budgetSubtypeFeignClient).addSubtype(budgetSubtypeDTO);
    }

    @Test
    void updateSubtype_Success() {
        when(budgetSubtypeFeignClient.updateSubtype(budgetSubtypeDTO)).thenReturn(new ResponseEntity<>(budgetSubtypeDTO, HttpStatus.OK));

        ResponseEntity<BudgetSubtypeDTO> response = budgetSubtypeFeignClient.updateSubtype(budgetSubtypeDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(budgetSubtypeDTO);
        verify(budgetSubtypeFeignClient).updateSubtype(budgetSubtypeDTO);
    }

    @Test
    void deleteSubtype_Success() {
        when(budgetSubtypeFeignClient.deleteSubtype(id)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        ResponseEntity<Void> response = budgetSubtypeFeignClient.deleteSubtype(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(budgetSubtypeFeignClient).deleteSubtype(id);
    }

    @Test
    void findSubtypeById_Success() {
        when(budgetSubtypeFeignClient.findSubtypeById(id)).thenReturn(new ResponseEntity<>(budgetSubtypeDTO, HttpStatus.OK));

        ResponseEntity<BudgetSubtypeDTO> response = budgetSubtypeFeignClient.findSubtypeById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(budgetSubtypeDTO);
        verify(budgetSubtypeFeignClient).findSubtypeById(id);
    }

    @Test
    void findAllSubtypes_Success() {
        Page<BudgetSubtypeDTO> page = new PageImpl<>(Collections.singletonList(budgetSubtypeDTO), pageable, 1);
        when(budgetSubtypeFeignClient.findAllSubtypes(pageable)).thenReturn(new ResponseEntity<>(page, HttpStatus.OK));

        ResponseEntity<Page<BudgetSubtypeDTO>> response = budgetSubtypeFeignClient.findAllSubtypes(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent().get(0)).isEqualTo(budgetSubtypeDTO);
        verify(budgetSubtypeFeignClient).findAllSubtypes(pageable);
    }
}
