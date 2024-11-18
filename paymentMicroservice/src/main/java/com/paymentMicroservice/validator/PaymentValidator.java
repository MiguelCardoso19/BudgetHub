package com.paymentMicroservice.validator;

import com.paymentMicroservice.dto.BudgetSubtypeDTO;
import com.paymentMicroservice.dto.BudgetTypeDTO;
import com.paymentMicroservice.dto.CreatePaymentDTO;
import com.paymentMicroservice.dto.CreatePaymentItemDTO;
import com.paymentMicroservice.exception.BudgetExceededException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@RequiredArgsConstructor
@Data
public class PaymentValidator {
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;

    private final ConcurrentHashMap<UUID, CompletableFuture<BudgetSubtypeDTO>> pendingSubtypeRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, CompletableFuture<BudgetTypeDTO>> pendingTypeRequests = new ConcurrentHashMap<>();

    @Value("${TIMEOUT_DURATION}")
    private long TIMEOUT_DURATION;

    public void validateFundsForPayment(CreatePaymentDTO createPaymentDTO) throws BudgetExceededException, ExecutionException, InterruptedException, TimeoutException {
        Map<UUID, Long> budgetSubtypeTotals = calculateBudgetTotals(createPaymentDTO.getItems(), true);
        Map<UUID, Long> budgetTypeTotals = calculateBudgetTotals(createPaymentDTO.getItems(), false);
        validateBudgetFunds(budgetSubtypeTotals, "find-budget-subtype-by-id", pendingSubtypeRequests, BudgetSubtypeDTO.class);
        validateBudgetFunds(budgetTypeTotals, "find-budget-type-by-id", pendingTypeRequests, BudgetTypeDTO.class);
    }

    public CompletableFuture<BudgetTypeDTO> removePendingTypeRequestById(UUID id) {
        return pendingTypeRequests.remove(id);
    }

    public CompletableFuture<BudgetSubtypeDTO> removePendingSubtypeRequestById(UUID id) {
        return pendingSubtypeRequests.remove(id);
    }

    private <T> void validateBudgetFunds(Map<UUID, Long> budgetTotals, String kafkaTopic, Map<UUID, CompletableFuture<T>> pendingRequests, Class<T> dtoClass) throws BudgetExceededException, ExecutionException, InterruptedException, TimeoutException {
        for (Map.Entry<UUID, Long> entry : budgetTotals.entrySet()) {
            UUID budgetID = entry.getKey();
            CompletableFuture<T> future = new CompletableFuture<>();
            pendingRequests.put(budgetID, future);
            kafkaUuidTemplate.send(kafkaTopic, budgetID);

            T dto = future.get(TIMEOUT_DURATION, SECONDS);
            if (dto != null && entry.getValue() > getAvailableFunds(dto)) {
                throw new BudgetExceededException(getAvailableFunds(dto), (double) entry.getValue());
            }
        }
    }

    private <T> double getAvailableFunds(T dto) {
        if (dto instanceof BudgetSubtypeDTO) {
            return ((BudgetSubtypeDTO) dto).getAvailableFunds();
        } else if (dto instanceof BudgetTypeDTO) {
            return ((BudgetTypeDTO) dto).getAvailableFunds();
        }
        return 0.0;
    }

    private Map<UUID, Long> calculateBudgetTotals(CreatePaymentItemDTO[] items, boolean isSubtype) {
        return Arrays.stream(items)
                .filter(item -> isSubtype ? item.getBudgetSubtypeId() != null : item.getBudgetTypeId() != null)
                .collect(Collectors.groupingBy(
                        isSubtype ? CreatePaymentItemDTO::getBudgetSubtypeId : CreatePaymentItemDTO::getBudgetTypeId,
                        Collectors.summingLong(item -> {
                            double ivaMultiplier = (item.getIvaRate() != null && item.getIvaRate() > 0) ? 1 + (item.getIvaRate() / 100) : 1;
                            return Math.round(item.getAmount() * ivaMultiplier);
                        })
                ));
    }
}
