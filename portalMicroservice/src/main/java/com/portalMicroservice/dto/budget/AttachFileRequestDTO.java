package com.portalMicroservice.dto.budget;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AttachFileRequestDTO extends AbstractDTO{

    @NotEmpty
    @NotEmpty
    private String base64File;
}
