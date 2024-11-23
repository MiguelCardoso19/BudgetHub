package com.portalMicroservice.dto.budget;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AttachFileRequestDTO {

    @Schema(description = "Unique identifier for the entity, typically assigned upon creation or retrieval.",
            example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID id;

    @Schema(description = "The base64 encoded file content to be attached.",
            example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA...")
    private String base64File;
}