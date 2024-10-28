package com.authenticationMicroservice.service.impl;

import com.authenticationMicroservice.dto.AuthenticationResponseDTO;
import com.authenticationMicroservice.dto.SignInRequestDTO;
import com.authenticationMicroservice.dto.UserCredentialsDTO;
import com.authenticationMicroservice.exception.EmailNotFoundException;
import com.authenticationMicroservice.exception.InvalidPasswordException;
import com.authenticationMicroservice.exception.NifNotFoundException;
import com.authenticationMicroservice.mapper.DTOMapper;
import com.authenticationMicroservice.model.UserCredentials;
import com.authenticationMicroservice.service.AuthenticationService;
import com.authenticationMicroservice.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final DTOMapper dtoMapper;
    private final UserCredentialsServiceImpl userCredentialsService;

    @Override
    public AuthenticationResponseDTO signIn(SignInRequestDTO signInRequestDTO) throws InvalidPasswordException, EmailNotFoundException {
            UserCredentials user = userCredentialsService.findByEmail(signInRequestDTO.getEmail())
                .orElseThrow(() -> new EmailNotFoundException(signInRequestDTO.getEmail()));

        if (!passwordEncoder.matches(signInRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        return dtoMapper.toDTO(jwtService.generateToken(user), jwtService.generateRefreshToken(user), user.getId());
    }

    @Override
    public AuthenticationResponseDTO refreshToken(HttpServletRequest request) throws NifNotFoundException {
        String authHeader = request.getHeader("Authorization");
        String refreshToken = authHeader.substring(7).trim();
        String nif = jwtService.extractNif(refreshToken);
        AuthenticationResponseDTO authenticationResponseDTO = null;

        UserCredentials user = userCredentialsService.findByNif(nif)
                .orElseThrow(() -> new NifNotFoundException(nif));

        if (jwtService.isTokenValid(refreshToken, nif)) {

            authenticationResponseDTO = dtoMapper.toDTOWithoutUserID(
                    jwtService.generateToken(user), refreshToken);

        }

        return authenticationResponseDTO;
    }
}