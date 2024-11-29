package com.authenticationMicroservice.service;

import com.authenticationMicroservice.dto.AuthenticationResponseDTO;
import com.authenticationMicroservice.dto.SignInRequestDTO;
import com.authenticationMicroservice.enumerator.UserStatus;
import com.authenticationMicroservice.exception.EmailNotFoundException;
import com.authenticationMicroservice.exception.InvalidPasswordException;
import com.authenticationMicroservice.exception.NifNotFoundException;
import com.authenticationMicroservice.mapper.DTOMapper;
import com.authenticationMicroservice.model.UserCredentials;
import com.authenticationMicroservice.service.impl.AuthenticationServiceImpl;
import com.authenticationMicroservice.service.impl.UserCredentialsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private DTOMapper dtoMapper;

    @Mock
    private UserCredentialsServiceImpl userCredentialsService;

    @Mock
    private HttpServletRequest request;

    private UserCredentials user;
    private SignInRequestDTO signInRequestDTO;

    private String refreshToken;

    private String nif;

    @BeforeEach
    void setUp() {
        openMocks(this);

        refreshToken = "refreshToken";
        nif = "123456789";

        user = new UserCredentials();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setNif("123456789");
        user.setStatus(UserStatus.LOGGED_OUT);

        signInRequestDTO = new SignInRequestDTO();
        signInRequestDTO.setEmail("test@example.com");
        signInRequestDTO.setPassword("plainPassword");
    }

    @Test
    void testSignIn_Success() throws InvalidPasswordException, EmailNotFoundException {
        when(userCredentialsService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");
        when(dtoMapper.toDTO("jwtToken", "refreshToken", user.getId()))
                .thenReturn(new AuthenticationResponseDTO("jwtToken", "refreshToken"));

        AuthenticationResponseDTO response = authenticationService.signIn(signInRequestDTO);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(userCredentialsService).save(user);
        assertEquals(UserStatus.LOGGED_IN, user.getStatus());
    }

    @Test
    void testSignIn_InvalidPassword() {
        when(userCredentialsService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> authenticationService.signIn(signInRequestDTO));
    }

    @Test
    void testSignIn_EmailNotFound() {
        when(userCredentialsService.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(EmailNotFoundException.class, () -> authenticationService.signIn(signInRequestDTO));
    }

    @Test
    void testRefreshToken_Success() throws NifNotFoundException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractNif(refreshToken)).thenReturn(nif);
        when(userCredentialsService.findByNif(nif)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("newJwtToken");
        when(dtoMapper.toDTOWithoutUserID("newJwtToken", refreshToken))
                .thenReturn(new AuthenticationResponseDTO("newJwtToken", refreshToken));

        AuthenticationResponseDTO response = authenticationService.refreshToken(request);

        assertNotNull(response);
        assertEquals("newJwtToken", response.getToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    void testRefreshToken_NifNotFound() {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractNif(refreshToken)).thenReturn(nif);
        when(userCredentialsService.findByNif(nif)).thenReturn(Optional.empty());

        assertThrows(NifNotFoundException.class, () -> authenticationService.refreshToken(request));
    }

    @Test
    void testSignOut_Success() throws NifNotFoundException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractNif(refreshToken)).thenReturn(nif);
        when(userCredentialsService.findByNif(nif)).thenReturn(Optional.of(user));

        authenticationService.signOut(request);

        verify(userCredentialsService).save(user);
        assertEquals(UserStatus.LOGGED_OUT, user.getStatus());
        verifyNoInteractions(dtoMapper); // No DTO interactions on signOut
    }

    @Test
    void testSignOut_NifNotFound() {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractNif(refreshToken)).thenReturn(nif);
        when(userCredentialsService.findByNif(nif)).thenReturn(Optional.empty());

        assertThrows(NifNotFoundException.class, () -> authenticationService.signOut(request));
    }
}
