package com.budgetMicroservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Getter
@Setter
public class Supplier extends AbstractEntity {

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String responsibleName;

    @Column(nullable = false)
    private String nif;

    private String phoneNumber;

    private String email;
}