package com.portalMicroservice.dto.budget;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AttachFileRequestDTO{
    private UUID id;
    private String base64File;
}
