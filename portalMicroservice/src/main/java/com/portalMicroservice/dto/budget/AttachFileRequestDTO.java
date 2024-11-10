package com.portalMicroservice.dto.budget;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AttachFileRequestDTO {

    @Schema(description = "The unique identifier for the attachment request.",
            example = "bce75d99-5481-4c1f-8211-3e4452d45b71")
    private UUID id;

    @Schema(description = "The base64 encoded file content to be attached.",
            example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA...")
    private String base64File;
}