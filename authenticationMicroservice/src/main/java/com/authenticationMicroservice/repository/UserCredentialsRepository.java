package com.authenticationMicroservice.repository;

import com.authenticationMicroservice.model.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, UUID> {
    Optional<UserCredentials> findByNif(String nif);
    Optional<UserCredentials> findByEmail(String email);
    boolean existsByNifAndIdNot(String nif, UUID id);
    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByNif(String nif);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, UUID id);
}