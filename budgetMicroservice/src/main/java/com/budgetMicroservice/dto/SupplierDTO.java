package com.budgetMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierDTO extends AbstractDTO {

    @NotBlank(message = "Company name is required")
    @JsonProperty("companyName")  // Optional if field names match
    private String companyName;

    @NotBlank(message = "Responsible name is required")
    @JsonProperty("responsibleName")
    private String responsibleName;

    @NotBlank(message = "NIF is required")
    @JsonProperty("nif")
    private String nif;

    @JsonProperty("phoneNumber")  // Optional
    private String phoneNumber;

    @Email(message = "Email should be valid")
    @JsonProperty("email")
    private String email;

    @JsonCreator
    public SupplierDTO(@JsonProperty("companyName") String companyName,
                       @JsonProperty("responsibleName") String responsibleName,
                       @JsonProperty("nif") String nif,
                       @JsonProperty("phoneNumber") String phoneNumber,
                       @JsonProperty("email") String email) {
        this.companyName = companyName;
        this.responsibleName = responsibleName;
        this.nif = nif;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
