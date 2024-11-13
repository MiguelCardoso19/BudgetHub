package com.authenticationMicroservice.service;

import com.authenticationMicroservice.dto.AuthenticationResponseDTO;
import com.authenticationMicroservice.dto.DeleteRequestDTO;
import com.authenticationMicroservice.dto.ResetPasswordRequestDTO;
import com.authenticationMicroservice.dto.UserCredentialsDTO;
import com.authenticationMicroservice.enumerator.UserStatus;
import com.authenticationMicroservice.exception.*;
import com.authenticationMicroservice.model.UserCredentials;

import java.util.Optional;

public interface UserCredentialsService {
    Optional<UserCredentials> findByNif(String nif);
    Optional<UserCredentials> findByEmail(String email);
    AuthenticationResponseDTO register(UserCredentialsDTO userCredentialsDTO) throws UserCredentialsValidationException;
    void delete(DeleteRequestDTO deleteRequestDTO) throws InvalidPasswordException, UserNotFoundException;
    UserCredentialsDTO update(UserCredentialsDTO userCredentialsDTO) throws UserCredentialsValidationException, InvalidPasswordException, UserNotFoundException;
    void save(UserCredentials userCredentials);
    void recoverPassword(String email) throws EmailNotFoundException;
    void resetPassword(ResetPasswordRequestDTO resetPasswordRequestDTO) throws InvalidTokenException;
    UserStatus getUserStatus(String nif);
}