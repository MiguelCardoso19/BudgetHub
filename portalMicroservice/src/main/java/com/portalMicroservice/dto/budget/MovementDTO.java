package com.portalMicroservice.dto.budget;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.portalMicroservice.enumerator.MovementStatus;
import com.portalMicroservice.enumerator.MovementType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

import static com.portalMicroservice.enumerator.MovementStatus.ACCEPTED;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovementDTO extends AbstractDTO {

    @NotNull
    @NotEmpty
    private UUID supplierId;

    private UUID budgetTypeId;

    private UUID budgetSubtypeId;

    private UUID invoiceId;

    private String documentNumber;

    @Enumerated(EnumType.STRING)
    private MovementType type;

    @NotNull
    @NotEmpty
    private LocalDate dateOfEmission;

    @NotNull
    @NotEmpty
    private String description;

    @NotNull
    @NotEmpty
    private Double valueWithoutIva;

    private Double ivaRate = 0.0;

    private Double ivaValue;

    private Double totalValue;

    @JsonProperty("status")
    @Enumerated(EnumType.STRING)
    private MovementStatus status = ACCEPTED;

    private InvoiceDTO invoice;

    private BudgetTypeDTO budgetType;

    private BudgetSubtypeDTO budgetSubtype;

    private SupplierDTO supplier;
}