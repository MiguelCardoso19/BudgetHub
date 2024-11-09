package com.portalMicroservice.dto.budget;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MovementsByBudgetRequestDTO {
    private UUID id;
    private CustomPageableDTO pageable;
}