package com.authenticationMicroservice.service.impl;

import com.authenticationMicroservice.dto.AuthenticationResponseDTO;
import com.authenticationMicroservice.dto.SignInRequestDTO;
import com.authenticationMicroservice.exception.EmailNotFoundException;
import com.authenticationMicroservice.exception.InvalidPasswordException;
import com.authenticationMicroservice.exception.NifNotFoundException;
import com.authenticationMicroservice.mapper.DTOMapper;
import com.authenticationMicroservice.model.UserCredentials;
import com.authenticationMicroservice.service.AuthenticationService;
import com.authenticationMicroservice.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.authenticationMicroservice.enumerator.UserStatus.LOGGED_IN;
import static com.authenticationMicroservice.enumerator.UserStatus.LOGGED_OUT;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final DTOMapper dtoMapper;
    private final UserCredentialsServiceImpl userCredentialsService;

    @Override
    public AuthenticationResponseDTO signIn(SignInRequestDTO signInRequestDTO) throws InvalidPasswordException, EmailNotFoundException {
        UserCredentials user = userCredentialsService.findByEmail(signInRequestDTO.getEmail()).orElseThrow(() -> new EmailNotFoundException(signInRequestDTO.getEmail()));

        if (!passwordEncoder.matches(signInRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        user.setStatus(LOGGED_IN);
        userCredentialsService.save(user);
        return dtoMapper.toDTO(jwtService.generateToken(user), jwtService.generateRefreshToken(user), user.getId(), user.getNif(), user.getFirstName());
    }

    @Override
    public AuthenticationResponseDTO refreshToken(HttpServletRequest request) throws NifNotFoundException {
        String refreshToken = request.getHeader("Authorization").replace("Bearer ", "");
        String nif = jwtService.extractNif(refreshToken);

        UserCredentials user = userCredentialsService.findByNif(nif).orElseThrow(() -> new NifNotFoundException(nif));

        return dtoMapper.toDTOWithoutUserID(jwtService.generateToken(user), refreshToken);
    }

    @Override
    public void signOut(HttpServletRequest request) throws NifNotFoundException {
        String nif = jwtService.extractNif(request.getHeader("Authorization").replace("Bearer ", ""));

        UserCredentials user = userCredentialsService.findByNif(nif).orElseThrow(() -> new NifNotFoundException(nif));

        user.setStatus(LOGGED_OUT);
        userCredentialsService.save(user);
        SecurityContextHolder.clearContext();
    }
}