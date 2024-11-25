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

    @Schema(description = "The MIME type (Content-Type) of the file being uploaded. For example, 'application/pdf' or 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' for Excel files.",
            example = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", required = true)
    private String contentType;
}