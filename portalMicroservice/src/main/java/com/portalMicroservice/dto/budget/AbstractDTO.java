package com.portalMicroservice.dto.budget;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AbstractDTO {

    @Schema(description = "Unique identifier (UUID) for each entity instance", example = "e12a567d-32f8-4e9a-9073-6781f9d5e423")
    private UUID id;

    private int version;
}