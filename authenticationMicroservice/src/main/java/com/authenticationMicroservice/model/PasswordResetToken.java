package com.authenticationMicroservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDateTime;

@Entity
@Data
@Audited
public class PasswordResetToken extends AbstractEntity {

    private String token;

    private LocalDateTime expiryDate;

    @NotAudited
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private UserCredentials user;
}