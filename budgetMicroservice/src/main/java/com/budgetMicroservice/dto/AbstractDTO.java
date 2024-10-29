package com.budgetMicroservice.dto;

import java.util.UUID;

public class AbstractDTO {
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}