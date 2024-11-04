package com.budgetMicroservice.repository;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.enumerator.MovementStatus;
import com.budgetMicroservice.model.Movement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovementRepository extends JpaRepository<Movement, UUID> {
    Page<Movement> findByBudgetSubtypeId(UUID budgetSubtypeId, Pageable pageable);
    Page<Movement> findByBudgetTypeId(UUID budgetTypeId, Pageable pageable);

    List<Movement> findByDateOfEmissionBetweenAndStatus(LocalDate startDate, LocalDate endDate, MovementStatus status);

    List<Movement> findByDateOfEmissionBetween(LocalDate startDate, LocalDate endDate);

    List<Movement> findByStatus(MovementStatus status);
    //  Optional<List<MovementDTO>> findByDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, String status);
}
