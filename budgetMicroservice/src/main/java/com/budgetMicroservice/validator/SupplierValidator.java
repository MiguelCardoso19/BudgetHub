package com.budgetMicroservice.validator;

import com.budgetMicroservice.dto.SupplierDTO;
import com.budgetMicroservice.exception.SupplierValidationException;
import com.budgetMicroservice.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SupplierValidator {

    public static void validateSupplierCreation(SupplierDTO supplierDTO, SupplierRepository repository)
            throws SupplierValidationException {

        List<String> errorMessages = new ArrayList<>();

        validateNifForExistingSupplier(supplierDTO, repository, errorMessages);
        validateEmailForExistingSupplier(supplierDTO, repository, errorMessages);
        validatePhoneNumberForExistingSupplier(supplierDTO, repository, errorMessages);
        validateCompanyNameForExistingSupplier(supplierDTO, repository, errorMessages);

        if (!errorMessages.isEmpty()) {
            throw new SupplierValidationException(errorMessages);
        }
    }

    public static void validateSupplierUpdate(SupplierDTO supplierDTO, SupplierRepository repository)
            throws SupplierValidationException {

        List<String> errorMessages = new ArrayList<>();

        validateNifForExistingSupplierUpdate(supplierDTO, repository, errorMessages);
        validateEmailForExistingSupplierUpdate(supplierDTO, repository, errorMessages);
        validatePhoneNumberForExistingSupplierUpdate(supplierDTO, repository, errorMessages);
        validateCompanyNameForExistingSupplierUpdate(supplierDTO, repository, errorMessages);

        if (!errorMessages.isEmpty()) {
            throw new SupplierValidationException(errorMessages);
        }
    }

    private static void validateNifForExistingSupplier(SupplierDTO supplierDTO,
                                                       SupplierRepository repository,
                                                       List<String> errorMessages) {
        log.info("Checking for existing NIF: {}", supplierDTO.getNif());

        if (repository.existsByNif(supplierDTO.getNif())) {
            errorMessages.add("NIF already exists: " + supplierDTO.getNif());
            log.error("Validation failed: NIF already exists. NIF: {}", supplierDTO.getNif());
        }
    }

    private static void validateEmailForExistingSupplier(SupplierDTO supplierDTO,
                                                         SupplierRepository repository,
                                                         List<String> errorMessages) {
        log.info("Checking for existing email: {}", supplierDTO.getEmail());

        if (repository.existsByEmail(supplierDTO.getEmail())) {
            errorMessages.add("Email already exists: " + supplierDTO.getEmail());
            log.error("Validation failed: Email already exists. Email: {}", supplierDTO.getEmail());
        }
    }

    private static void validatePhoneNumberForExistingSupplier(SupplierDTO supplierDTO,
                                                               SupplierRepository repository,
                                                               List<String> errorMessages) {
        log.info("Checking for existing phone number: {}", supplierDTO.getPhoneNumber());

        if (repository.existsByPhoneNumber(supplierDTO.getPhoneNumber())) {
            errorMessages.add("This phone number already exists: " + supplierDTO.getPhoneNumber());
            log.error("Validation failed: Phone number already exists. Phone number: {}", supplierDTO.getPhoneNumber());
        }
    }

    private static void validateCompanyNameForExistingSupplier(SupplierDTO supplierDTO,
                                                               SupplierRepository repository,
                                                               List<String> errorMessages) {
        log.info("Checking for existing company name: {}", supplierDTO.getCompanyName());

        if (repository.existsByCompanyName(supplierDTO.getCompanyName())) {
            errorMessages.add("This company name already exists: " + supplierDTO.getCompanyName());
            log.error("Validation failed: Company name already exists. Company name: {}", supplierDTO.getCompanyName());
        }
    }

    private static void validateNifForExistingSupplierUpdate(SupplierDTO supplierDTO,
                                                             SupplierRepository repository,
                                                             List<String> errorMessages) {
        log.info("Checking for existing NIF (update): {}", supplierDTO.getNif());

        if (repository.existsByNifAndIdNot(supplierDTO.getNif(), supplierDTO.getId())) {
            errorMessages.add("NIF already exists: " + supplierDTO.getNif());
            log.error("Validation failed: NIF already exists. NIF: {}", supplierDTO.getNif());
        }
    }

    private static void validateEmailForExistingSupplierUpdate(SupplierDTO supplierDTO,
                                                               SupplierRepository repository,
                                                               List<String> errorMessages) {
        log.info("Checking for existing email (update): {}", supplierDTO.getEmail());

        if (repository.existsByEmailAndIdNot(supplierDTO.getEmail(), supplierDTO.getId())) {
            errorMessages.add("Email already exists: " + supplierDTO.getEmail());
            log.error("Validation failed: Email already exists. Email: {}", supplierDTO.getEmail());
        }
    }

    private static void validatePhoneNumberForExistingSupplierUpdate(SupplierDTO supplierDTO,
                                                                     SupplierRepository repository,
                                                                     List<String> errorMessages) {
        log.info("Checking for existing phone number (update): {}", supplierDTO.getPhoneNumber());

        if (repository.existsByPhoneNumberAndIdNot(supplierDTO.getPhoneNumber(), supplierDTO.getId())) {
            errorMessages.add("This phone number already exists: " + supplierDTO.getPhoneNumber());
            log.error("Validation failed: Phone number already exists. Phone number: {}", supplierDTO.getPhoneNumber());
        }
    }

    private static void validateCompanyNameForExistingSupplierUpdate(SupplierDTO supplierDTO,
                                                                     SupplierRepository repository,
                                                                     List<String> errorMessages) {
        log.info("Checking for existing company name (update): {}", supplierDTO.getCompanyName());

        if (repository.existsByCompanyNameAndIdNot(supplierDTO.getCompanyName(), supplierDTO.getId())) {
            errorMessages.add("This company name already exists: " + supplierDTO.getCompanyName());
            log.error("Validation failed: Company name already exists. Company name: {}", supplierDTO.getCompanyName());
        }
    }
}