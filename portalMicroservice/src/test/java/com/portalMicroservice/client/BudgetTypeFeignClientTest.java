package com.portalMicroservice.client;

import com.portalMicroservice.client.budget.BudgetTypeFeignClient;
import com.portalMicroservice.dto.budget.BudgetTypeDTO;
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

class BudgetTypeFeignClientTest {

    @Mock
    private BudgetTypeFeignClient budgetTypeFeignClient;

    private BudgetTypeDTO budgetTypeDTO;

    private UUID id;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        openMocks(this);

        budgetTypeDTO = new BudgetTypeDTO();
        budgetTypeDTO.setId(UUID.randomUUID());
        budgetTypeDTO.setName("Test Budget Type");

        id = UUID.randomUUID();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void createBudgetType_Success() {
        when(budgetTypeFeignClient.createBudgetType(budgetTypeDTO)).thenReturn(new ResponseEntity<>(budgetTypeDTO, HttpStatus.CREATED));

        ResponseEntity<BudgetTypeDTO> response = budgetTypeFeignClient.createBudgetType(budgetTypeDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(budgetTypeDTO);
        verify(budgetTypeFeignClient).createBudgetType(budgetTypeDTO);
    }

    @Test
    void deleteBudgetType_Success() {
        when(budgetTypeFeignClient.deleteBudgetType(id)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        ResponseEntity<Void> response = budgetTypeFeignClient.deleteBudgetType(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(budgetTypeFeignClient).deleteBudgetType(id);
    }

    @Test
    void updateBudgetType_Success() {
        when(budgetTypeFeignClient.updateBudgetType(budgetTypeDTO)).thenReturn(new ResponseEntity<>(budgetTypeDTO, HttpStatus.OK));

        ResponseEntity<BudgetTypeDTO> response = budgetTypeFeignClient.updateBudgetType(budgetTypeDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(budgetTypeDTO);
        verify(budgetTypeFeignClient).updateBudgetType(budgetTypeDTO);
    }

    @Test
    void findBudgetTypeById_Success() {
        when(budgetTypeFeignClient.findBudgetTypeById(id)).thenReturn(new ResponseEntity<>(budgetTypeDTO, HttpStatus.OK));

        ResponseEntity<BudgetTypeDTO> response = budgetTypeFeignClient.findBudgetTypeById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(budgetTypeDTO);
        verify(budgetTypeFeignClient).findBudgetTypeById(id);
    }

    @Test
    void findAllBudgetTypes_Success() {
        Page<BudgetTypeDTO> page = new PageImpl<>(Collections.singletonList(budgetTypeDTO), pageable, 1);
        when(budgetTypeFeignClient.findAllBudgetTypes(pageable)).thenReturn(new ResponseEntity<>(page, HttpStatus.OK));

        ResponseEntity<Page<BudgetTypeDTO>> response = budgetTypeFeignClient.findAllBudgetTypes(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent().get(0)).isEqualTo(budgetTypeDTO);
        verify(budgetTypeFeignClient).findAllBudgetTypes(pageable);
    }
}