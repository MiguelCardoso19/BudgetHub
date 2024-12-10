package com.notificationMicroservice.service;

import java.io.IOException;

public interface S3Service {
    String putObject(byte[] fileBytes, String fileName);
    void deleteObject(String fileKey);
    byte[] getObject(String fileKey) throws IOException;
}