package com.budgetMicroservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface S3Service {
    String putObject(MultipartFile file) throws IOException;
    InputStream getObject(String fileKey);
    void deleteObject(String fileKey);
}