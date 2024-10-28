package com.authenticationMicroservice.validator;

import com.authenticationMicroservice.dto.UserCredentialsDTO;
import com.authenticationMicroservice.exception.UserCredentialsValidationException;
import com.authenticationMicroservice.repository.UserCredentialsRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserCredentialsValidator {

    public static void validateUserCredentials(UserCredentialsDTO userCredentialsDTO,
                                               UserCredentialsRepository repository) throws UserCredentialsValidationException {

        List<String> errorMessages = new ArrayList<>();

        validateNifForExistingUser(userCredentialsDTO, repository, errorMessages);
        validateEmailForExistingUser(userCredentialsDTO, repository, errorMessages);
        validatePhoneNumberForExistingUser(userCredentialsDTO, repository, errorMessages);

        if (!errorMessages.isEmpty()) {
            throw new UserCredentialsValidationException(errorMessages);
        }
    }

    private static void validateNifForExistingUser(UserCredentialsDTO userCredentialsDTO,
                                                   UserCredentialsRepository repository,
                                                   List<String> errorMessages) {
        log.info("Checking for existing NIF: {}", userCredentialsDTO.getNif());

        if (repository.existsByNifAndIdNot(userCredentialsDTO.getNif(), userCredentialsDTO.getId())) {
            errorMessages.add("NIF already exists: " + userCredentialsDTO.getNif());
            log.error("Validation failed: NIF already exists. NIF: {}", userCredentialsDTO.getNif());
        }
    }

    private static void validateEmailForExistingUser(UserCredentialsDTO userCredentialsDTO,
                                                     UserCredentialsRepository repository,
                                                     List<String> errorMessages) {
        log.info("Checking for existing email: {}", userCredentialsDTO.getEmail());

        if (repository.existsByEmailAndIdNot(userCredentialsDTO.getEmail(), userCredentialsDTO.getId())) {
            errorMessages.add("Email already exists: " + userCredentialsDTO.getEmail());
            log.error("Validation failed: Email already exists. Email: {}", userCredentialsDTO.getEmail());
        }
    }

    private static void validatePhoneNumberForExistingUser(UserCredentialsDTO userCredentialsDTO,
                                                           UserCredentialsRepository repository,
                                                           List<String> errorMessages) {
        log.info("Checking for existing email: {}", userCredentialsDTO.getPhoneNumber());

        if (repository.existsByEmailAndIdNot(userCredentialsDTO.getPhoneNumber(), userCredentialsDTO.getId())) {
            errorMessages.add("This phone number already exists: " + userCredentialsDTO.getPhoneNumber());
            log.error("Validation failed: Email already exists. Email: {}", userCredentialsDTO.getPhoneNumber());
        }
    }
}