package com.paymentMicroservice.service;

import com.paymentMicroservice.dto.BudgetSubtypeDTO;
import com.paymentMicroservice.dto.BudgetTypeDTO;
import com.paymentMicroservice.service.impl.PaymentEventListenerServiceImpl;
import com.paymentMicroservice.validator.PaymentValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class PaymentEventListenerServiceImplTest {

    @Mock
    private PaymentValidator paymentValidator;

    @InjectMocks
    private PaymentEventListenerServiceImpl paymentEventListenerService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testHandleBudgetSubtypeResponse() {
        BudgetSubtypeDTO budgetSubtypeDTO = mock(BudgetSubtypeDTO.class);
        CompletableFuture<BudgetSubtypeDTO> future = mock(CompletableFuture.class);
        when(paymentValidator.removePendingSubtypeRequestById(budgetSubtypeDTO.getId())).thenReturn(future);

        paymentEventListenerService.handleBudgetSubtypeResponse(budgetSubtypeDTO);

        verify(paymentValidator, times(1)).removePendingSubtypeRequestById(budgetSubtypeDTO.getId());
        verify(future, times(1)).complete(budgetSubtypeDTO);
    }

    @Test
    void testHandleBudgetTypeResponse() {
        BudgetTypeDTO budgetTypeDTO = mock(BudgetTypeDTO.class);
        CompletableFuture<BudgetTypeDTO> future = mock(CompletableFuture.class);
        when(paymentValidator.removePendingTypeRequestById(budgetTypeDTO.getId())).thenReturn(future);

        paymentEventListenerService.handleBudgetTypeResponse(budgetTypeDTO);

        verify(paymentValidator, times(1)).removePendingTypeRequestById(budgetTypeDTO.getId());
        verify(future, times(1)).complete(budgetTypeDTO);
    }

    @Test
    void testHandleBudgetSubtypeResponse_withNullFuture() {
        BudgetSubtypeDTO budgetSubtypeDTO = mock(BudgetSubtypeDTO.class);
        when(paymentValidator.removePendingSubtypeRequestById(budgetSubtypeDTO.getId())).thenReturn(null);

        paymentEventListenerService.handleBudgetSubtypeResponse(budgetSubtypeDTO);

        verify(paymentValidator, times(1)).removePendingSubtypeRequestById(budgetSubtypeDTO.getId());
    }

    @Test
    void testHandleBudgetTypeResponse_withNullFuture() {
        BudgetTypeDTO budgetTypeDTO = mock(BudgetTypeDTO.class);
        when(paymentValidator.removePendingTypeRequestById(budgetTypeDTO.getId())).thenReturn(null);

        paymentEventListenerService.handleBudgetTypeResponse(budgetTypeDTO);

        verify(paymentValidator, times(1)).removePendingTypeRequestById(budgetTypeDTO.getId());
    }
}