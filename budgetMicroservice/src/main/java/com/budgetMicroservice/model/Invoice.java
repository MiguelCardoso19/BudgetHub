package com.budgetMicroservice.model;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDate;

@Entity
@Audited
public class Invoice extends AbstractEntity {

    @Column(nullable = false)
    private LocalDate dateOfEmission;

    @Column(nullable = false)
    private String documentNumber;

    @Column(nullable = false)
    private String description;

    @NotAudited
    @OneToOne
    @JoinColumn(name = "movement_id")
    private Movement movement;

    @Lob
    private byte[] file;

    public LocalDate getDateOfEmission() {
        return dateOfEmission;
    }

    public void setDateOfEmission(LocalDate dateOfEmission) {
        this.dateOfEmission = dateOfEmission;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
