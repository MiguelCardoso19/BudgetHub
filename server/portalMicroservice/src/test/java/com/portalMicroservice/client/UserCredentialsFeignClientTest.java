package com.portalMicroservice.client;

import com.portalMicroservice.client.authentication.UserCredentialsFeignClient;
import com.portalMicroservice.dto.authentication.*;
import com.portalMicroservice.enumerator.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static com.portalMicroservice.enumerator.UserStatus.LOGGED_IN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserCredentialsFeignClientTest {

    @Mock
    private UserCredentialsFeignClient userCredentialsFeignClient;

    private UserCredentialsDTO userCredentialsDTO;

    private DeleteRequestDTO deleteRequestDTO;

    private ResetPasswordRequestDTO resetPasswordRequestDTO;

    private AuthenticationResponseDTO authenticationResponseDTO;

    private String email;

    private String nif;

    @BeforeEach
    void setUp() {
        openMocks(this);

        email = "test@example.com";
        nif = "123456789";

        userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setEmail(email);
        userCredentialsDTO.setPassword("testPassword");

        deleteRequestDTO = new DeleteRequestDTO();
        deleteRequestDTO.setId(UUID.randomUUID());

        resetPasswordRequestDTO = new ResetPasswordRequestDTO();
        resetPasswordRequestDTO.setNewPassword("newPassword");
        resetPasswordRequestDTO.setToken("resetToken");

        authenticationResponseDTO = new AuthenticationResponseDTO();
        authenticationResponseDTO.setToken("dummyToken");
    }

    @Test
    void register_Success() {
        when(userCredentialsFeignClient.register(userCredentialsDTO)).thenReturn(new ResponseEntity<>(authenticationResponseDTO, HttpStatus.CREATED));

        ResponseEntity<AuthenticationResponseDTO> response = userCredentialsFeignClient.register(userCredentialsDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(authenticationResponseDTO);
        verify(userCredentialsFeignClient).register(userCredentialsDTO);
    }

    @Test
    void update_Success() {
        when(userCredentialsFeignClient.update(userCredentialsDTO)).thenReturn(new ResponseEntity<>(userCredentialsDTO, HttpStatus.OK));

        ResponseEntity<UserCredentialsDTO> response = userCredentialsFeignClient.update(userCredentialsDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userCredentialsDTO);
        verify(userCredentialsFeignClient).update(userCredentialsDTO);
    }

    @Test
    void delete_Success() {
        when(userCredentialsFeignClient.delete(deleteRequestDTO)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        ResponseEntity<Void> response = userCredentialsFeignClient.delete(deleteRequestDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(userCredentialsFeignClient).delete(deleteRequestDTO);
    }

    @Test
    void recoverPassword_Success() {
        when(userCredentialsFeignClient.recoverPassword(email)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<Void> response = userCredentialsFeignClient.recoverPassword(email);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userCredentialsFeignClient).recoverPassword(email);
    }

    @Test
    void resetPassword_Success() {
        when(userCredentialsFeignClient.resetPassword(resetPasswordRequestDTO)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<Void> response = userCredentialsFeignClient.resetPassword(resetPasswordRequestDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userCredentialsFeignClient).resetPassword(resetPasswordRequestDTO);
    }

    @Test
    void getUserStatus_Success() {
        when(userCredentialsFeignClient.getUserStatus(nif)).thenReturn(LOGGED_IN);

        UserStatus actualStatus = userCredentialsFeignClient.getUserStatus(nif);

        assertThat(actualStatus).isEqualTo(LOGGED_IN);
        verify(userCredentialsFeignClient).getUserStatus(nif);
    }
}