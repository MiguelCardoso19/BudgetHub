package com.budgetMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierDTO extends AbstractDTO implements Serializable {

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Responsible name is required")
    private String responsibleName;

    @NotBlank(message = "NIF is required")
    private String nif;

    private String phoneNumber;

    @Email(message = "Email should be valid")
    private String email;
}
