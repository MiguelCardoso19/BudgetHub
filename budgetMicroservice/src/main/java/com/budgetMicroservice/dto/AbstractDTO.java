package com.budgetMicroservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AbstractDTO {
    private UUID id;
    private int version;
}