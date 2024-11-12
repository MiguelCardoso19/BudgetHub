package com.authenticationMicroservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDateTime;

@Entity
@Data
@Audited
@RequiredArgsConstructor
public class PasswordResetToken extends AbstractEntity {

    private String token;

    private LocalDateTime expiryDate;

    @NotAudited
    @ManyToOne
    private UserCredentials user;
}