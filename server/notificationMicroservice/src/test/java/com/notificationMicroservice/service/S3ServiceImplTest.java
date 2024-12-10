package com.notificationMicroservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.notificationMicroservice.service.impl.S3ServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

import java.io.IOException;

class S3ServiceImplTest {

    @Mock
    private AmazonS3 amazonS3;

    @InjectMocks
    private S3ServiceImpl s3ServiceImpl;

    @Value("${S3.BUCKET.NAME}")
    private String bucketName;

    @Captor
    private ArgumentCaptor<PutObjectRequest> putObjectRequestCaptor;

    @Captor
    private ArgumentCaptor<DeleteObjectRequest> deleteObjectRequestCaptor;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testPutObject() {
        byte[] fileContent = "test content".getBytes();
        String fileKey = "test-file.xlsx";

        s3ServiceImpl.putObject(fileContent, fileKey);

        verify(amazonS3).putObject(putObjectRequestCaptor.capture());
        PutObjectRequest capturedPutObjectRequest = putObjectRequestCaptor.getValue();

        assertEquals(bucketName, capturedPutObjectRequest.getBucketName());
        assertEquals(fileKey, capturedPutObjectRequest.getKey());
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", capturedPutObjectRequest.getMetadata().getContentType());
        assertEquals(fileContent.length, capturedPutObjectRequest.getMetadata().getContentLength());
    }

    @Test
    void testGetObject() throws IOException {
        String fileKey = "test-file.xlsx";
        byte[] expectedContent = "test content".getBytes();

        S3Object s3Object = mock(S3Object.class);
        S3ObjectInputStream s3ObjectInputStream = mock(S3ObjectInputStream.class);

        when(amazonS3.getObject(bucketName, fileKey)).thenReturn(s3Object);
        when(s3Object.getObjectContent()).thenReturn(s3ObjectInputStream);
        when(s3ObjectInputStream.readAllBytes()).thenReturn(expectedContent);

        byte[] actualContent = s3ServiceImpl.getObject(fileKey);

        assertArrayEquals(expectedContent, actualContent);
    }

    @Test
    void testDeleteObject() {
        String fileKey = "test-file.xlsx";

        s3ServiceImpl.deleteObject(fileKey);

        verify(amazonS3).deleteObject(deleteObjectRequestCaptor.capture());
        DeleteObjectRequest capturedDeleteObjectRequest = deleteObjectRequestCaptor.getValue();

        assertEquals(bucketName, capturedDeleteObjectRequest.getBucketName());
        assertEquals(fileKey, capturedDeleteObjectRequest.getKey());
    }
}