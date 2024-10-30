package com.budgetMicroservice.repository;

import com.budgetMicroservice.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovementRepository extends JpaRepository<Movement, UUID> {
    boolean existsByDocumentNumberAndIdNot(String documentNumber, UUID id);
    boolean existsByDocumentNumber(String documentNumber);
}
