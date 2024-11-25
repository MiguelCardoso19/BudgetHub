package com.budgetMicroservice.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.budgetMicroservice.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final AmazonS3 s3Client;

    @Value("${S3.BUCKET.NAME}")
    private String bucketName;

    @Override
    public String uploadFileToS3(MultipartFile file) throws IOException {
        File tempFile = convertMultiPartToFile(file);
        s3Client.putObject(new PutObjectRequest(bucketName, file.getOriginalFilename(), tempFile));
        return file.getOriginalFilename();
    }

    @Override
    public InputStream getFileFromS3(String fileKey) {
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, fileKey));
        return s3Object.getObjectContent();
    }

    @Override
    public void deleteFileFromS3(String fileKey) {
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
}
