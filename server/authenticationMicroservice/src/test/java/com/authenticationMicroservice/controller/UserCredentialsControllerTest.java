package com.authenticationMicroservice.controller;

import com.authenticationMicroservice.dto.*;
import com.authenticationMicroservice.enumerator.UserStatus;
import com.authenticationMicroservice.exception.*;
import com.authenticationMicroservice.service.impl.UserCredentialsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserCredentialsControllerTest {

    @Mock
    private UserCredentialsServiceImpl userCredentialsService;

    @InjectMocks
    private UserCredentialsController userCredentialsController;

    private UserCredentialsDTO userCredentialsDTO;

    private AuthenticationResponseDTO responseDTO;

    private DeleteRequestDTO deleteRequestDTO;

    private String email;

    @BeforeEach
    void setUp() {
        openMocks(this);
        deleteRequestDTO = new DeleteRequestDTO();
        userCredentialsDTO = new UserCredentialsDTO();
        responseDTO = new AuthenticationResponseDTO();
        email = "notfound@test.com";
    }

    @Test
    void testRegister_Success() throws UserCredentialsValidationException {
        when(userCredentialsService.register(userCredentialsDTO)).thenReturn(responseDTO);

        ResponseEntity<AuthenticationResponseDTO> response = userCredentialsController.register(userCredentialsDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(responseDTO, response.getBody());
        verify(userCredentialsService, times(1)).register(userCredentialsDTO);
    }

    @Test
    void testUpdate_Success() throws UserCredentialsValidationException, InvalidPasswordException, UserNotFoundException {
        when(userCredentialsService.update(userCredentialsDTO)).thenReturn(userCredentialsDTO);

        ResponseEntity<UserCredentialsDTO> response = userCredentialsController.update(userCredentialsDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(userCredentialsDTO, response.getBody());
        verify(userCredentialsService, times(1)).update(userCredentialsDTO);
    }

    @Test
    void testDelete_Success() throws InvalidPasswordException, UserNotFoundException {
        ResponseEntity<Void> response = userCredentialsController.delete(deleteRequestDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        verify(userCredentialsService, times(1)).delete(deleteRequestDTO);
    }

    @Test
    void testRecoverPassword_Success() throws EmailNotFoundException {
        ResponseEntity<Void> response = userCredentialsController.recoverPassword(email);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        verify(userCredentialsService, times(1)).recoverPassword(email);
    }

    @Test
    void testResetPassword_Success() throws InvalidTokenException {
        ResetPasswordRequestDTO resetPasswordRequestDTO = new ResetPasswordRequestDTO();

        ResponseEntity<Void> response = userCredentialsController.resetPassword(resetPasswordRequestDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        verify(userCredentialsService, times(1)).resetPassword(resetPasswordRequestDTO);
    }

    @Test
    void testGetUserStatus_Success() {
        String nif = "12345678A";
        UserStatus status = UserStatus.LOGGED_IN;

        when(userCredentialsService.getUserStatus(nif)).thenReturn(status);

        ResponseEntity<UserStatus> response = userCredentialsController.getUserStatus(nif);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(status, response.getBody());
        verify(userCredentialsService, times(1)).getUserStatus(nif);
    }

    @Test
    void testRegister_UserCredentialsValidationException() throws UserCredentialsValidationException {
        when(userCredentialsService.register(userCredentialsDTO)).thenThrow(new UserCredentialsValidationException(new ArrayList<>()));

        assertThrows(UserCredentialsValidationException.class, () -> userCredentialsController.register(userCredentialsDTO));
        verify(userCredentialsService, times(1)).register(userCredentialsDTO);
    }

    @Test
    void testUpdate_UserNotFoundException() throws UserCredentialsValidationException, InvalidPasswordException, UserNotFoundException {
        when(userCredentialsService.update(userCredentialsDTO)).thenThrow(new UserNotFoundException(UUID.randomUUID()));

        assertThrows(UserNotFoundException.class, () -> userCredentialsController.update(userCredentialsDTO));
        verify(userCredentialsService, times(1)).update(userCredentialsDTO);
    }

    @Test
    void testDelete_InvalidPasswordException() throws InvalidPasswordException, UserNotFoundException {
        doThrow(new InvalidPasswordException()).when(userCredentialsService).delete(deleteRequestDTO);

        assertThrows(InvalidPasswordException.class, () -> userCredentialsController.delete(deleteRequestDTO));
        verify(userCredentialsService, times(1)).delete(deleteRequestDTO);
    }

    @Test
    void testRecoverPassword_EmailNotFoundException() throws EmailNotFoundException {
        doThrow(new EmailNotFoundException(email)).when(userCredentialsService).recoverPassword(email);

        assertThrows(EmailNotFoundException.class, () -> userCredentialsController.recoverPassword(email));
        verify(userCredentialsService, times(1)).recoverPassword(email);
    }
}
