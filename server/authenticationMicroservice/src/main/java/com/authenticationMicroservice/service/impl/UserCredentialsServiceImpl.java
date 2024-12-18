package com.authenticationMicroservice.service.impl;

import com.authenticationMicroservice.dto.*;
import com.authenticationMicroservice.enumerator.UserStatus;
import com.authenticationMicroservice.exception.*;
import com.authenticationMicroservice.mapper.DTOMapper;
import com.authenticationMicroservice.model.PasswordResetToken;
import com.authenticationMicroservice.model.UserCredentials;
import com.authenticationMicroservice.repository.PasswordResetTokenRepository;
import com.authenticationMicroservice.repository.UserCredentialsRepository;
import com.authenticationMicroservice.service.JwtService;
import com.authenticationMicroservice.service.UserCredentialsService;
import com.authenticationMicroservice.validator.UserCredentialsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCredentialsServiceImpl implements UserCredentialsService {
    private final UserCredentialsRepository userCredentialsRepository;
    private final DTOMapper dtoMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final KafkaTemplate<String, NotificationRequestDTO> kafkaNotificationRequestTemplate;

    @Override
    public AuthenticationResponseDTO register(UserCredentialsDTO userCredentialsDTO) throws UserCredentialsValidationException {
        UserCredentialsValidator.validateUserCredentialsCreation(userCredentialsDTO, userCredentialsRepository);
        UserCredentials newUser = dtoMapper.toEntity(userCredentialsDTO, passwordEncoder);
        userCredentialsRepository.save(newUser);
        return dtoMapper.toDTO(jwtService.generateToken(newUser), jwtService.generateRefreshToken(newUser), newUser.getId());
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) throws InvalidPasswordException, UserNotFoundException {
        UserCredentials existingUser = userCredentialsRepository.findById(deleteRequestDTO.getId())
                .orElseThrow(() -> new UserNotFoundException(deleteRequestDTO.getId()));

        if (!passwordEncoder.matches(deleteRequestDTO.getPassword(), existingUser.getPassword())) {
            throw new InvalidPasswordException();
        }

        userCredentialsRepository.delete(existingUser);
    }

    @Override
    public UserCredentialsDTO update(UserCredentialsDTO userCredentialsDTO) throws UserCredentialsValidationException, InvalidPasswordException, UserNotFoundException {
        UserCredentials existingUser = userCredentialsRepository.findById(userCredentialsDTO.getId())
                .orElseThrow(() -> new UserNotFoundException(userCredentialsDTO.getId()));

        if (!passwordEncoder.matches(userCredentialsDTO.getPassword(), existingUser.getPassword())) {
            throw new InvalidPasswordException();
        }

        UserCredentialsValidator.validateUserCredentialsUpdate(userCredentialsDTO, userCredentialsRepository);

        if (userCredentialsDTO.getNewPassword() != null) {
            dtoMapper.updateFromDTO(userCredentialsDTO, existingUser, passwordEncoder);
        } else {
            dtoMapper.updateFromDTO(userCredentialsDTO, existingUser);
        }
        return dtoMapper.toDTO(userCredentialsRepository.save(existingUser));
    }

    @Override
    public void recoverPassword(String email) throws EmailNotFoundException {
        UserCredentials user = userCredentialsRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email));

        String resetToken = jwtService.generateResetPasswordToken(user);
        PasswordResetToken tokenEntity = new PasswordResetToken();
        tokenEntity.setToken(resetToken);
        tokenEntity.setExpiryDate(LocalDateTime.now().plusHours(1));
        tokenEntity.setUser(user);
        passwordResetTokenRepository.save(tokenEntity);

        String resetLink = "http://localhost:4200/reset-password?token=" + resetToken;
        kafkaNotificationRequestTemplate.send("notification-reset-password-topic", new NotificationRequestDTO(user.getEmail(), resetLink));
    }

    @Override
    public void resetPassword(ResetPasswordRequestDTO resetPasswordRequestDTO) throws InvalidTokenException {
        PasswordResetToken tokenEntity = passwordResetTokenRepository.findByToken(resetPasswordRequestDTO.getToken())
                .orElseThrow(InvalidTokenException::new);

        if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException();
        }

        UserCredentials user = tokenEntity.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordRequestDTO.getNewPassword()));
        userCredentialsRepository.save(user);
    }

    @Override
    public Optional<UserCredentials> findByNif(String nif) {
        return userCredentialsRepository.findByNif(nif);
    }

    @Override
    public Optional<UserCredentials> findByEmail(String email) {
        return userCredentialsRepository.findByEmail(email);
    }

    @Override
    public void save(UserCredentials userCredentials) {
        userCredentialsRepository.save(userCredentials);
    }

    @Override
    public UserStatus getUserStatus(String nif) {
        return userCredentialsRepository.findByNif(nif)
                .map(UserCredentials::getStatus)
                .orElse(UserStatus.LOGGED_OUT);
    }

    @Override
    public UserCredentialsDTO findById(UUID id) throws UserNotFoundException {
        return dtoMapper.toDTO(userCredentialsRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }
}