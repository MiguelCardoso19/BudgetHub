package com.authenticationMicroservice.dto;

import com.authenticationMicroservice.enumerator.NationalityEnum;
import com.authenticationMicroservice.enumerator.UserGenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Transfer Object representing user credentials, used for registration and update.")
public class UserCredentialsDTO extends AbstractDTO {

    @NotNull
    @NotEmpty
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;

    @NotNull
    @NotEmpty
    @Schema(description = "Last name of the user", example = "Keen", required = true)
    private String lastName;

    @NotNull
    @NotEmpty
    @Schema(description = "Tax Identification Number (NIF) of the user", example = "123456789", required = true)
    private String nif;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email format")
    @Schema(description = "Email address of the user", example = "john.keen@example.com", required = true)
    private String email;

    @Schema(description = "Password of the user", example = "P@ssw0rd123", required = true)
    private String password;

    @Schema(description = "New password for updating credentials (if applicable)", example = "NewP@ssw0rd123")
    private String newPassword;

    @NotNull
    @Schema(description = "Date of birth of the user in YYYY-MM-DD format", example = "1990-01-01", required = true)
    @Past
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotNull
    @Schema(description = "Nationality of the user", example = "Portuguese", required = true)
    private NationalityEnum nationality;

    @NotNull
    @Schema(description = "Gender of the user", example = "Male", required = true)
    private UserGenderEnum gender;

    @NotNull
    @NotEmpty
    @Schema(description = "Phone number of the user", example = "912954239", required = true)
    private String phoneNumber;

    @NotNull
    @NotEmpty
    @Schema(description = "Roles assigned to the user", example = "[\"ADMIN\", \"EMPLOYEE\"]", required = true)
    private Set<String> roles;
}