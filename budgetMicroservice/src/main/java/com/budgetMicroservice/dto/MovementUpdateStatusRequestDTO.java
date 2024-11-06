package com.budgetMicroservice.dto;

import com.budgetMicroservice.enumerator.MovementStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovementUpdateStatusRequestDTO extends AbstractDTO{
    @Enumerated(EnumType.STRING)
    MovementStatus status;
}
