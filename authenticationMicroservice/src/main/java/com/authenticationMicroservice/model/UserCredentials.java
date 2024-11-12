package com.authenticationMicroservice.model;

import com.authenticationMicroservice.enumerator.NationalityEnum;
import com.authenticationMicroservice.enumerator.UserGenderEnum;
import com.authenticationMicroservice.enumerator.UserRoleEnum;
import com.authenticationMicroservice.enumerator.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.Set;

import static com.authenticationMicroservice.enumerator.UserStatus.LOGGED_OUT;

@Entity
@Data
@Audited
public class UserCredentials extends AbstractEntity{

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true, length = 9)
    private String nif;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NationalityEnum nationality;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserGenderEnum gender;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = LOGGED_OUT;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<UserRoleEnum> roles;
}