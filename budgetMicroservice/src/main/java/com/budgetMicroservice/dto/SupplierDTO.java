package com.budgetMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Transfer Object representing a supplier, containing details about the company, responsible person, and contact information.")
public class SupplierDTO extends AbstractDTO {

    @NotBlank(message = "Company name is required")
    @Schema(description = "The name of the supplier company.",
            example = "Tech Solutions Ltd.", required = true)
    private String companyName;

    @NotBlank(message = "Responsible name is required")
    @Schema(description = "The name of the person responsible for the supplier.",
            example = "John Doe", required = true)
    private String responsibleName;

    @NotBlank(message = "NIF is required")
    @Schema(description = "The NIF (Tax Identification Number) of the supplier.",
            example = "123456789", required = true)
    private String nif;

    @Schema(description = "The phone number of the supplier.",
            example = "+1234567890")
    private String phoneNumber;

    @Email(message = "Email should be valid")
    @Schema(description = "The email address of the supplier.",
            example = "supplier@example.com")
    private String email;
}
