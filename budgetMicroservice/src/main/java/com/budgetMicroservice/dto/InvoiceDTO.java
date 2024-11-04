package com.budgetMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceDTO extends AbstractDTO {

    @NotNull
    @NotEmpty
    private LocalDate dateOfEmission;

    @NotNull
    @NotEmpty
    private String description;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID movementId;

    @Lob
    private byte[] file;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private MovementDTO movement;
}
