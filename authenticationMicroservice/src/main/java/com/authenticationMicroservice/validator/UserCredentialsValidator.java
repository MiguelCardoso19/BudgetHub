package com.authenticationMicroservice.validator;

import com.authenticationMicroservice.dto.UserCredentialsDTO;
import com.authenticationMicroservice.exception.UserCredentialsValidationException;
import com.authenticationMicroservice.repository.UserCredentialsRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserCredentialsValidator {

    public static void validateUserCredentialsCreation(UserCredentialsDTO userCredentialsDTO,
                                                       UserCredentialsRepository repository)
            throws UserCredentialsValidationException {

        List<String> errorMessages = new ArrayList<>();

        validateNifForExistingUserCreation(userCredentialsDTO, repository, errorMessages);
        validateEmailForExistingUserCreation(userCredentialsDTO, repository, errorMessages);
        validatePhoneNumberForExistingUserCreation(userCredentialsDTO, repository, errorMessages);

        if (!errorMessages.isEmpty()) {
            throw new UserCredentialsValidationException(errorMessages);
        }
    }

    public static void validateUserCredentialsUpdate(UserCredentialsDTO userCredentialsDTO,
                                                     UserCredentialsRepository repository)
            throws UserCredentialsValidationException {

        List<String> errorMessages = new ArrayList<>();

        validateNifForExistingUserUpdate(userCredentialsDTO, repository, errorMessages);
        validateEmailForExistingUserUpdate(userCredentialsDTO, repository, errorMessages);
        validatePhoneNumberForExistingUserUpdate(userCredentialsDTO, repository, errorMessages);

        if (!errorMessages.isEmpty()) {
            throw new UserCredentialsValidationException(errorMessages);
        }
    }

    private static void validateNifForExistingUserCreation(UserCredentialsDTO userCredentialsDTO,
                                                           UserCredentialsRepository repository,
                                                           List<String> errorMessages) {
        log.info("Checking for existing NIF (creation): {}", userCredentialsDTO.getNif());

        if (repository.existsByNif(userCredentialsDTO.getNif())) {
            errorMessages.add("NIF already exists: " + userCredentialsDTO.getNif());
            log.error("Validation failed: NIF already exists. NIF: {}", userCredentialsDTO.getNif());
        }
    }

    private static void validateEmailForExistingUserCreation(UserCredentialsDTO userCredentialsDTO,
                                                             UserCredentialsRepository repository,
                                                             List<String> errorMessages) {
        log.info("Checking for existing email (creation): {}", userCredentialsDTO.getEmail());

        if (repository.existsByEmail(userCredentialsDTO.getEmail())) {
            errorMessages.add("Email already exists: " + userCredentialsDTO.getEmail());
            log.error("Validation failed: Email already exists. Email: {}", userCredentialsDTO.getEmail());
        }
    }

    private static void validatePhoneNumberForExistingUserCreation(UserCredentialsDTO userCredentialsDTO,
                                                                   UserCredentialsRepository repository,
                                                                   List<String> errorMessages) {
        log.info("Checking for existing phone number (creation): {}", userCredentialsDTO.getPhoneNumber());

        if (repository.existsByPhoneNumber(userCredentialsDTO.getPhoneNumber())) {
            errorMessages.add("This phone number already exists: " + userCredentialsDTO.getPhoneNumber());
            log.error("Validation failed: Phone number already exists. Phone number: {}", userCredentialsDTO.getPhoneNumber());
        }
    }

    private static void validateNifForExistingUserUpdate(UserCredentialsDTO userCredentialsDTO,
                                                         UserCredentialsRepository repository,
                                                         List<String> errorMessages) {
        log.info("Checking for existing NIF (update): {}", userCredentialsDTO.getNif());

        if (repository.existsByNifAndIdNot(userCredentialsDTO.getNif(), userCredentialsDTO.getId())) {
            errorMessages.add("NIF already exists: " + userCredentialsDTO.getNif());
            log.error("Validation failed: NIF already exists. NIF: {}", userCredentialsDTO.getNif());
        }
    }

    private static void validateEmailForExistingUserUpdate(UserCredentialsDTO userCredentialsDTO,
                                                           UserCredentialsRepository repository,
                                                           List<String> errorMessages) {
        log.info("Checking for existing email (update): {}", userCredentialsDTO.getEmail());

        if (repository.existsByEmailAndIdNot(userCredentialsDTO.getEmail(), userCredentialsDTO.getId())) {
            errorMessages.add("Email already exists: " + userCredentialsDTO.getEmail());
            log.error("Validation failed: Email already exists. Email: {}", userCredentialsDTO.getEmail());
        }
    }

    private static void validatePhoneNumberForExistingUserUpdate(UserCredentialsDTO userCredentialsDTO,
                                                                 UserCredentialsRepository repository,
                                                                 List<String> errorMessages) {
        log.info("Checking for existing phone number (update): {}", userCredentialsDTO.getPhoneNumber());

        if (repository.existsByPhoneNumberAndIdNot(userCredentialsDTO.getPhoneNumber(), userCredentialsDTO.getId())) {
            errorMessages.add("This phone number already exists: " + userCredentialsDTO.getPhoneNumber());
            log.error("Validation failed: Phone number already exists. Phone number: {}", userCredentialsDTO.getPhoneNumber());
        }
    }
}