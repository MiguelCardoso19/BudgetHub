package com.portalMicroservice.dto.budget;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "Base Data Transfer Object class that contains common fields like correlation ID, unique ID, and version.")
public class AbstractDTO {

    @Schema(description = "Unique correlation ID to trace and associate requests/responses.",
            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479", required = true)
    private UUID correlationId;

    @Schema(description = "Unique identifier for the entity, typically assigned upon creation or retrieval.",
            example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID id;

    @Schema(description = "Version number for optimistic locking and concurrent updates management.",
            example = "1", required = true)
    private int version;
}
