package com.notificationMicroservice.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.notificationMicroservice.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final AmazonS3 s3Client;

    @Value("${S3.BUCKET.NAME}")
    private String bucketName;

    public String putObject(byte[] fileContent, String fileKey) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        metadata.setContentLength(fileContent.length);
        s3Client.putObject(new PutObjectRequest(bucketName, fileKey, new ByteArrayInputStream(fileContent), metadata));
        return fileKey;
    }

    @Override
    public byte[] getObject(String fileKey) throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, fileKey);
        return s3Object.getObjectContent().readAllBytes();
    }

    @Override
    public void deleteObject(String fileKey) {
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
    }
}