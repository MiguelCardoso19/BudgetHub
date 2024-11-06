package com.budgetMicroservice.dto;

import com.budgetMicroservice.enumerator.MovementStatus;
import com.budgetMicroservice.enumerator.MovementType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

import static com.budgetMicroservice.enumerator.MovementStatus.ACCEPTED;

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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private InvoiceDTO invoice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BudgetTypeDTO budgetType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BudgetSubtypeDTO budgetSubtype;

    private SupplierDTO supplier;
}