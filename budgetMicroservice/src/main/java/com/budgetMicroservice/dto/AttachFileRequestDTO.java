package com.budgetMicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "DTO representing a file attachment request, containing either a base64 encoded file or a multipart file.")
public class AttachFileRequestDTO extends AbstractDTO {

    @Schema(description = "Base64 encoded file content. This is an optional field that provides a way to send files as a string.",
            example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA...",
            nullable = true)
    private String base64File;

    @Schema(description = "Multipart file for uploading, typically used for larger files or when direct file upload is needed.",
            nullable = true)
    private MultipartFile multipartFile;

    @Schema(description = "The MIME type (Content-Type) of the file being uploaded. For example, 'application/pdf' or 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' for Excel files.",
            example = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", required = true)
    private String contentType;
}
