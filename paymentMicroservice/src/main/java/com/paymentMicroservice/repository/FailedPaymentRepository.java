package com.paymentMicroservice.repository;

import com.paymentMicroservice.model.FailedPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FailedPaymentRepository extends JpaRepository<FailedPayment, UUID> {
    List<FailedPayment> findByRetryableTrue();
}