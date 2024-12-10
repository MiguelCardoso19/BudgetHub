package com.authenticationMicroservice.controller;

import com.authenticationMicroservice.dto.AuthenticationResponseDTO;
import com.authenticationMicroservice.dto.SignInRequestDTO;
import com.authenticationMicroservice.exception.EmailNotFoundException;
import com.authenticationMicroservice.exception.InvalidPasswordException;
import com.authenticationMicroservice.exception.NifNotFoundException;
import com.authenticationMicroservice.exception.UserNotFoundException;
import com.authenticationMicroservice.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private SignInRequestDTO signInRequestDTO;

    private AuthenticationResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        openMocks(this);
        signInRequestDTO = new SignInRequestDTO();
        signInRequestDTO.setEmail("example@gmail.com");
        signInRequestDTO.setPassword("fakedata");
        responseDTO = new AuthenticationResponseDTO();
        responseDTO.setToken("accessToken");
        responseDTO.setRefreshToken("refreshToken");
    }

    @Test
    void testSignIn_Success() throws InvalidPasswordException, UserNotFoundException, EmailNotFoundException {
        when(authenticationService.signIn(signInRequestDTO)).thenReturn(responseDTO);

        ResponseEntity<AuthenticationResponseDTO> response = authenticationController.signIn(signInRequestDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(responseDTO, response.getBody());
        verify(authenticationService, times(1)).signIn(signInRequestDTO);
    }

    @Test
    void testSignIn_InvalidPassword() throws InvalidPasswordException, UserNotFoundException, EmailNotFoundException {
        when(authenticationService.signIn(signInRequestDTO)).thenThrow(new InvalidPasswordException());

        assertThrows(InvalidPasswordException.class, () -> authenticationController.signIn(signInRequestDTO));
        verify(authenticationService, times(1)).signIn(signInRequestDTO);
    }

    @Test
    void testRefreshToken_Success() throws IOException, UserNotFoundException, NifNotFoundException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(authenticationService.refreshToken(request)).thenReturn(responseDTO);

        ResponseEntity<AuthenticationResponseDTO> response = authenticationController.refreshToken(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(responseDTO, response.getBody());
        verify(authenticationService, times(1)).refreshToken(request);
    }

    @Test
    void testRefreshToken_InvalidToken() throws IOException, UserNotFoundException, NifNotFoundException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(authenticationService.refreshToken(request)).thenThrow(new NifNotFoundException("fakedata"));

        assertThrows(NifNotFoundException.class, () -> authenticationController.refreshToken(request));
        verify(authenticationService, times(1)).refreshToken(request);
    }

    @Test
    void testSignOut_Success() throws NifNotFoundException {
        HttpServletRequest request = mock(HttpServletRequest.class);

        ResponseEntity<Void> response = authenticationController.signOut(request);

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
        verify(authenticationService, times(1)).signOut(request);
    }

    @Test
    void testSignOut_NifNotFound() throws NifNotFoundException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        doThrow(new NifNotFoundException("fakedata")).when(authenticationService).signOut(request);

        assertThrows(NifNotFoundException.class, () -> authenticationController.signOut(request));
        verify(authenticationService, times(1)).signOut(request);
    }
}
