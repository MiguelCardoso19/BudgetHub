package com.authenticationMicroservice.service;

import com.authenticationMicroservice.dto.AuthenticationResponseDTO;
import com.authenticationMicroservice.dto.SignInRequestDTO;
import com.authenticationMicroservice.exception.EmailNotFoundException;
import com.authenticationMicroservice.exception.InvalidPasswordException;
import com.authenticationMicroservice.exception.NifNotFoundException;
import com.authenticationMicroservice.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponseDTO signIn(SignInRequestDTO signInRequestDTO) throws InvalidPasswordException, UserNotFoundException, EmailNotFoundException;
    AuthenticationResponseDTO refreshToken(HttpServletRequest request) throws IOException, UserNotFoundException, NifNotFoundException;
}