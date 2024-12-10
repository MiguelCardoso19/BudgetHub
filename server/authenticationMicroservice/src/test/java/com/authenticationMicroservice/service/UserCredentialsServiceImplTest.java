package com.authenticationMicroservice.service;

import com.authenticationMicroservice.dto.*;
import com.authenticationMicroservice.enumerator.UserStatus;
import com.authenticationMicroservice.exception.*;
import com.authenticationMicroservice.mapper.DTOMapper;
import com.authenticationMicroservice.model.PasswordResetToken;
import com.authenticationMicroservice.model.UserCredentials;
import com.authenticationMicroservice.repository.PasswordResetTokenRepository;
import com.authenticationMicroservice.repository.UserCredentialsRepository;
import com.authenticationMicroservice.service.impl.UserCredentialsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserCredentialsServiceImplTest {

    @InjectMocks
    private UserCredentialsServiceImpl userCredentialsService;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private DTOMapper dtoMapper;

    @Mock
    private KafkaTemplate<String, NotificationRequestDTO> kafkaNotificationRequestTemplate;

    private UserCredentials user;

    @BeforeEach
    void setUp() {
        openMocks(this);
        user = new UserCredentials();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setNif("123456789");
        user.setPassword("encodedPassword");
        user.setStatus(UserStatus.LOGGED_OUT);
    }

    @Test
    void testRegister_Success() throws UserCredentialsValidationException {
        UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setEmail("test@example.com");
        userCredentialsDTO.setPassword("plainPassword");

        UserCredentials newUser = new UserCredentials();
        when(dtoMapper.toEntity(userCredentialsDTO, passwordEncoder)).thenReturn(newUser);
        when(jwtService.generateToken(newUser)).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(newUser)).thenReturn("refreshToken");
        when(dtoMapper.toDTO("jwtToken", "refreshToken", newUser.getId()))
                .thenReturn(new AuthenticationResponseDTO("jwtToken", "refreshToken"));

        AuthenticationResponseDTO response = userCredentialsService.register(userCredentialsDTO);

        verify(userCredentialsRepository).save(newUser);
        assertEquals("jwtToken", response.getToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    void testDelete_Success() throws InvalidPasswordException, UserNotFoundException {
        UUID userId = UUID.randomUUID();
        DeleteRequestDTO deleteRequestDTO = new DeleteRequestDTO();
        deleteRequestDTO.setId(userId);
        deleteRequestDTO.setPassword("plainPassword");

        when(userCredentialsRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);

        userCredentialsService.delete(deleteRequestDTO);

        verify(userCredentialsRepository).delete(user);
    }

    @Test
    void testDelete_InvalidPassword() {
        UUID userId = UUID.randomUUID();
        DeleteRequestDTO deleteRequestDTO = new DeleteRequestDTO();
        deleteRequestDTO.setId(userId);
        deleteRequestDTO.setPassword("wrongPassword");

        when(userCredentialsRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> userCredentialsService.delete(deleteRequestDTO));
    }

    @Test
    void testDelete_UserNotFound() {
        DeleteRequestDTO deleteRequestDTO = new DeleteRequestDTO();

        when(userCredentialsRepository.findById(UUID.randomUUID())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userCredentialsService.delete(deleteRequestDTO));
    }

    @Test
    void testRecoverPassword_Success() throws EmailNotFoundException {
        when(userCredentialsRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateResetPasswordToken(user)).thenReturn("resetToken");

        userCredentialsService.recoverPassword("test@example.com");

        verify(passwordResetTokenRepository).save(any(PasswordResetToken.class));
        verify(kafkaNotificationRequestTemplate).send(eq("notification-reset-password-topic"), any(NotificationRequestDTO.class));
    }

    @Test
    void testRecoverPassword_EmailNotFound() {
        when(userCredentialsRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(EmailNotFoundException.class, () -> userCredentialsService.recoverPassword("test@example.com"));
    }

    @Test
    void testResetPassword_Success() throws InvalidTokenException {
        ResetPasswordRequestDTO resetPasswordRequestDTO = new ResetPasswordRequestDTO();
        resetPasswordRequestDTO.setToken("validToken");
        resetPasswordRequestDTO.setNewPassword("newPassword");

        PasswordResetToken tokenEntity = new PasswordResetToken();
        tokenEntity.setToken("validToken");
        tokenEntity.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        tokenEntity.setUser(user);

        when(passwordResetTokenRepository.findByToken("validToken")).thenReturn(Optional.of(tokenEntity));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        userCredentialsService.resetPassword(resetPasswordRequestDTO);

        verify(userCredentialsRepository).save(user);
        verify(passwordResetTokenRepository).delete(tokenEntity);
        verify(passwordEncoder).encode("newPassword");
        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    void testResetPassword_InvalidToken() {
        ResetPasswordRequestDTO resetPasswordRequestDTO = new ResetPasswordRequestDTO();

        when(passwordResetTokenRepository.findByToken("invalidToken")).thenReturn(Optional.empty());

        assertThrows(InvalidTokenException.class, () -> userCredentialsService.resetPassword(resetPasswordRequestDTO));
    }

    @Test
    void testResetPassword_ExpiredToken() {
        ResetPasswordRequestDTO resetPasswordRequestDTO = new ResetPasswordRequestDTO();

        PasswordResetToken tokenEntity = new PasswordResetToken();
        tokenEntity.setToken("expiredToken");
        tokenEntity.setExpiryDate(LocalDateTime.now().minusMinutes(30));

        when(passwordResetTokenRepository.findByToken("expiredToken")).thenReturn(Optional.of(tokenEntity));

        assertThrows(InvalidTokenException.class, () -> userCredentialsService.resetPassword(resetPasswordRequestDTO));
    }

    @Test
    void testFindByNif() {
        when(userCredentialsRepository.findByNif("123456789")).thenReturn(Optional.of(user));

        Optional<UserCredentials> result = userCredentialsService.findByNif("123456789");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testFindByEmail() {
        when(userCredentialsRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<UserCredentials> result = userCredentialsService.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testSave() {
        userCredentialsService.save(user);

        verify(userCredentialsRepository).save(user);
    }

    @Test
    void testGetUserStatus() {
        when(userCredentialsRepository.findByNif("123456789")).thenReturn(Optional.of(user));

        UserStatus status = userCredentialsService.getUserStatus("123456789");

        assertEquals(UserStatus.LOGGED_OUT, status);
    }
}
