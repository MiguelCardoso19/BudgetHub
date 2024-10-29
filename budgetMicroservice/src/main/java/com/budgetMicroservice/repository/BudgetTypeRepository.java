package com.budgetMicroservice.repository;

import com.budgetMicroservice.model.BudgetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BudgetTypeRepository extends JpaRepository<BudgetType, UUID> {
}
