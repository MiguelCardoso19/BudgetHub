package com.budgetMicroservice.controller;

import com.budgetMicroservice.dto.*;
import com.budgetMicroservice.enumerator.MovementStatus;
import com.budgetMicroservice.service.MovementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovementControllerTest {

    @MockBean
    private MovementService movementService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MovementDTO movementDTO;

    private UUID movementId;

    @BeforeEach
    void setUp() {
        movementId = UUID.randomUUID();
        movementDTO = new MovementDTO();
        movementDTO.setId(movementId);
        movementDTO.setDescription("Test movement");
    }

    @Test
    void testCreateMovement() throws Exception {
        when(movementService.create(Mockito.any(MovementDTO.class))).thenReturn(movementDTO);

        mockMvc.perform(post("/api/v1/movement/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movementDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movementId.toString()))
                .andExpect(jsonPath("$.description").value("Test movement"));

        verify(movementService, times(1)).create(Mockito.any(MovementDTO.class));
    }

    @Test
    void testUpdateMovement() throws Exception {
        when(movementService.update(Mockito.any(MovementDTO.class))).thenReturn(movementDTO);

        mockMvc.perform(put("/api/v1/movement/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movementDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movementId.toString()))
                .andExpect(jsonPath("$.description").value("Test movement"));

        verify(movementService, times(1)).update(Mockito.any(MovementDTO.class));
    }

    @Test
    void testUpdateMovementStatus() throws Exception {
        MovementUpdateStatusRequestDTO statusRequest = new MovementUpdateStatusRequestDTO();
        statusRequest.setId(movementId);
        statusRequest.setStatus(MovementStatus.ACCEPTED);
        when(movementService.updateMovementStatus(Mockito.any(MovementUpdateStatusRequestDTO.class))).thenReturn(movementDTO);

        mockMvc.perform(put("/api/v1/movement/status/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movementId.toString()));

        verify(movementService, times(1)).updateMovementStatus(Mockito.any(MovementUpdateStatusRequestDTO.class));
    }

    @Test
    void testGetMovementById() throws Exception {
        when(movementService.getMovementDTOById(movementId)).thenReturn(movementDTO);

        mockMvc.perform(get("/api/v1/movement/{id}", movementId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movementId.toString()))
                .andExpect(jsonPath("$.description").value("Test movement"));

        verify(movementService, times(1)).getMovementDTOById(movementId);
    }

    @Test
    void testDeleteMovement() throws Exception {
        doNothing().when(movementService).delete(movementId);

        mockMvc.perform(delete("/api/v1/movement/{id}", movementId))
                .andExpect(status().isNoContent());

        verify(movementService, times(1)).delete(movementId);
    }

    @Test
    void testGetAllMovements() throws Exception {
        mockMvc.perform(get("/api/v1/movement/all")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(movementService, times(1)).getAll(Mockito.any());
    }

    @Test
    void testExportMovementsReport() throws Exception {
        mockMvc.perform(post("/api/v1/movement/export-movements-report")
                        .param("userEmail", "test@example.com")
                        .param("startDate", "2024-11-22")
                        .param("endDate", "2024-11-29"))
                .andExpect(status().isOk());

        verify(movementService, times(1)).exportMovements(Mockito.any(ExportMovementsRequestDTO.class));
    }
}
