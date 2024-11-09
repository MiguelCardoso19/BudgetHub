package com.budgetMicroservice.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AttachFileRequestDTO extends AbstractDTO{

    private String base64File;

    private MultipartFile multipartFile;
}
