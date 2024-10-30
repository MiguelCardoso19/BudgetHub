package com.authenticationMicroservice.service.impl;


import com.authenticationMicroservice.dto.AuthenticationResponseDTO;
import com.authenticationMicroservice.dto.DeleteRequestDTO;
import com.authenticationMicroservice.dto.UserCredentialsDTO;
import com.authenticationMicroservice.exception.InvalidPasswordException;
import com.authenticationMicroservice.exception.UserCredentialsValidationException;
import com.authenticationMicroservice.exception.UserNotFoundException;
import com.authenticationMicroservice.mapper.DTOMapper;
import com.authenticationMicroservice.model.UserCredentials;
import com.authenticationMicroservice.repository.UserCredentialsRepository;
import com.authenticationMicroservice.service.JwtService;
import com.authenticationMicroservice.service.UserCredentialsService;
import com.authenticationMicroservice.validator.UserCredentialsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCredentialsServiceImpl implements UserCredentialsService {
    private final UserCredentialsRepository userCredentialsRepository;
    private final DTOMapper dtoMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponseDTO register(UserCredentialsDTO userCredentialsDTO) throws UserCredentialsValidationException {
        UserCredentialsValidator.validateUserCredentialsCreation(userCredentialsDTO, userCredentialsRepository);

        UserCredentials newUser = dtoMapper.toEntity(userCredentialsDTO, passwordEncoder);
        userCredentialsRepository.save(newUser);

        String token = jwtService.generateToken(newUser);
        String refreshToken = jwtService.generateRefreshToken(newUser);
        return dtoMapper.toDTO(refreshToken, token, newUser.getId());
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

        dtoMapper.updateFromDTO(userCredentialsDTO, existingUser, passwordEncoder);

        return dtoMapper.toDTO(userCredentialsRepository.save(existingUser));
    }

    @Override
    public Optional<UserCredentials> findByNif(String nif) {
        return userCredentialsRepository.findByNif(nif);
    }

    @Override
    public Optional<UserCredentials> findByEmail(String email) {
        return userCredentialsRepository.findByEmail(email);
    }
}