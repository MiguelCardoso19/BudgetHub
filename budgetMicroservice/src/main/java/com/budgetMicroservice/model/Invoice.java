package com.budgetMicroservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDate;

@Entity
@Audited
@Getter
@Setter
public class Invoice extends AbstractEntity {

    @Column(nullable = false)
    private LocalDate dateOfEmission;

    @Column(nullable = false)
    private String description;

    @NotAudited
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "invoice")
    @JoinColumn(name = "movement_id")
    private Movement movement;

    @Lob
    private byte[] file;
}