package com.portalMicroservice.client;

import com.portalMicroservice.client.authentication.AuthenticationFeignClient;
import com.portalMicroservice.dto.authentication.AuthenticationResponseDTO;
import com.portalMicroservice.dto.authentication.SignInRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AuthenticationFeignClientTest {

    @Mock
    private AuthenticationFeignClient authenticationFeignClient;

    private SignInRequestDTO signInRequestDTO;

    private AuthenticationResponseDTO authenticationResponseDTO;

    private String authHeader;

    @BeforeEach
    void setUp() {
        openMocks(this);

        signInRequestDTO = new SignInRequestDTO();
        signInRequestDTO.setEmail("example@example.com");
        signInRequestDTO.setPassword("testPassword");

        authenticationResponseDTO = new AuthenticationResponseDTO();
        authenticationResponseDTO.setToken("dummy-token");

        authHeader = "Bearer dummy-token";
    }

    @Test
    void signIn_Success() {
        when(authenticationFeignClient.signIn(signInRequestDTO))
                .thenReturn(new ResponseEntity<>(authenticationResponseDTO, HttpStatus.OK));

        ResponseEntity<AuthenticationResponseDTO> response = authenticationFeignClient.signIn(signInRequestDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(authenticationResponseDTO);
        verify(authenticationFeignClient).signIn(signInRequestDTO);
    }

    @Test
    void refreshToken_Success() {
        when(authenticationFeignClient.refreshToken(authHeader))
                .thenReturn(new ResponseEntity<>(authenticationResponseDTO, HttpStatus.OK));

        ResponseEntity<AuthenticationResponseDTO> response = authenticationFeignClient.refreshToken(authHeader);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(authenticationResponseDTO);
        verify(authenticationFeignClient).refreshToken(authHeader);
    }

    @Test
    void signOut_Success() {
        when(authenticationFeignClient.signOut(authHeader)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        ResponseEntity<Void> response = authenticationFeignClient.signOut(authHeader);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(authenticationFeignClient).signOut(authHeader);
    }
}
