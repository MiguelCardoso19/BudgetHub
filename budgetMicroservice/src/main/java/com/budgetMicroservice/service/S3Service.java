package com.budgetMicroservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface S3Service {
    String uploadFileToS3(MultipartFile file) throws IOException;
    InputStream getFileFromS3(String fileKey);
    void deleteFileFromS3(String fileKey);
}