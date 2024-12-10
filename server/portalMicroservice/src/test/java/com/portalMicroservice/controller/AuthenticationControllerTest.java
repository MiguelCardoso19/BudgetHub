package com.portalMicroservice.controller;

import com.portalMicroservice.client.authentication.AuthenticationFeignClient;
import com.portalMicroservice.controller.authentication.AuthenticationController;
import com.portalMicroservice.dto.authentication.AuthenticationResponseDTO;
import com.portalMicroservice.dto.authentication.SignInRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.HttpStatus.*;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationFeignClient authenticationFeignClient;

    @InjectMocks
    private AuthenticationController authenticationController;

    private HttpServletRequest mockRequest;

    private String mockToken;

    private AuthenticationResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        openMocks(this);
        mockToken = "Bearer mockSignOutToken";
        mockRequest = mock(HttpServletRequest.class);
        responseDTO = new AuthenticationResponseDTO();
    }

    @Test
    void testSignIn_Success() {
        SignInRequestDTO signInRequestDTO = new SignInRequestDTO();
        signInRequestDTO.setEmail("testUser@example.com");
        signInRequestDTO.setPassword("testPassword");

        responseDTO.setToken("mockToken");

        when(authenticationFeignClient.signIn(signInRequestDTO))
                .thenReturn(ResponseEntity.ok(responseDTO));

        ResponseEntity<AuthenticationResponseDTO> response = authenticationController.signIn(signInRequestDTO);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mockToken", response.getBody().getToken());
        verify(authenticationFeignClient, times(1)).signIn(signInRequestDTO);
    }

    @Test
    void testRefreshToken_Success() {
        responseDTO.setToken("newMockToken");

        when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(mockToken);
        when(authenticationFeignClient.refreshToken(mockToken))
                .thenReturn(ResponseEntity.ok(responseDTO));

        ResponseEntity<AuthenticationResponseDTO> response = authenticationController.refreshToken(mockRequest);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("newMockToken", response.getBody().getToken());
        verify(mockRequest, times(1)).getHeader(HttpHeaders.AUTHORIZATION);
        verify(authenticationFeignClient, times(1)).refreshToken(mockToken);
    }

    @Test
    void testSignOut_Success() {
        when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(mockToken);

        when(authenticationFeignClient.signOut(mockToken)).thenReturn(ResponseEntity.noContent().build());

        ResponseEntity<Void> response = authenticationController.signOut(mockRequest);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(mockRequest, times(1)).getHeader(HttpHeaders.AUTHORIZATION);
        verify(authenticationFeignClient, times(1)).signOut(mockToken);
    }
}
