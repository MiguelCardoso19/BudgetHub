package com.portalMicroservice.dto.budget;

import com.portalMicroservice.enumerator.MovementStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExportMovementsRequestDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private MovementStatus status;
    private String userEmail;
}
