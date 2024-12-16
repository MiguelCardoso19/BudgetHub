package com.portalMicroservice.controller;

import com.portalMicroservice.client.authentication.UserCredentialsFeignClient;
import com.portalMicroservice.controller.authentication.UserCredentialsController;
import com.portalMicroservice.dto.authentication.AuthenticationResponseDTO;
import com.portalMicroservice.dto.authentication.DeleteRequestDTO;
import com.portalMicroservice.dto.authentication.ResetPasswordRequestDTO;
import com.portalMicroservice.dto.authentication.UserCredentialsDTO;
import com.portalMicroservice.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserCredentialsControllerTest {

    @Mock
    private UserCredentialsFeignClient userCredentialsFeignClient;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserCredentialsController userCredentialsController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testRegister_Success() {
        UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setEmail("test@example.com");
        userCredentialsDTO.setPassword("password123");
        AuthenticationResponseDTO mockResponse = new AuthenticationResponseDTO();
        mockResponse.setToken("mockJwtToken");
        when(userCredentialsFeignClient.register(userCredentialsDTO)).thenReturn(ResponseEntity.ok(mockResponse));

        ResponseEntity<AuthenticationResponseDTO> response = userCredentialsController.register(userCredentialsDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(userCredentialsFeignClient, times(1)).register(userCredentialsDTO);
    }

    @Test
    void testUpdate_Success() {
        UserCredentialsDTO updatedCredentials = new UserCredentialsDTO();
        updatedCredentials.setEmail("updated@example.com");
        updatedCredentials.setPassword("newPassword123");
        when(userCredentialsFeignClient.update(updatedCredentials)).thenReturn(ResponseEntity.ok(updatedCredentials));

        ResponseEntity<UserCredentialsDTO> response = userCredentialsController.update(updatedCredentials);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCredentials, response.getBody());
        verify(userCredentialsFeignClient, times(1)).update(updatedCredentials);
    }

    @Test
    void testDelete_Success() {
        DeleteRequestDTO deleteRequestDTO = new DeleteRequestDTO();
        deleteRequestDTO.setId(UUID.randomUUID());
        when(userCredentialsFeignClient.delete(deleteRequestDTO)).thenReturn(ResponseEntity.noContent().build());

        ResponseEntity<Void> response = userCredentialsController.delete(deleteRequestDTO);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userCredentialsFeignClient, times(1)).delete(deleteRequestDTO);
    }

    @Test
    void testRecoverPassword_Success() {
        String email = "test@example.com";
        when(jwtService.getEmailFromRequest()).thenReturn(email);

        ResponseEntity<Void> expectedResponse = ResponseEntity.noContent().build();
        when(userCredentialsFeignClient.recoverPassword(email)).thenReturn(expectedResponse);

        ResponseEntity<Void> response = userCredentialsController.recoverPassword(email);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(jwtService, times(1)).getEmailFromRequest();
        verify(userCredentialsFeignClient, times(1)).recoverPassword(email);
    }

    @Test
    void testResetPassword_Success() {
        ResetPasswordRequestDTO resetPasswordRequestDTO = new ResetPasswordRequestDTO();
        resetPasswordRequestDTO.setToken("mockResetToken");
        resetPasswordRequestDTO.setNewPassword("newPassword123");

        ResponseEntity<Void> expectedResponse = ResponseEntity.noContent().build();
        when(userCredentialsFeignClient.resetPassword(resetPasswordRequestDTO)).thenReturn(expectedResponse);

        ResponseEntity<Void> response = userCredentialsController.resetPassword(resetPasswordRequestDTO);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userCredentialsFeignClient, times(1)).resetPassword(resetPasswordRequestDTO);
    }
}