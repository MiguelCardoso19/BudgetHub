package com.budgetMicroservice.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.budgetMicroservice.service.impl.S3ServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class S3ServiceImplTest {

    @Mock
    private AmazonS3 s3Client;

    @InjectMocks
    private S3ServiceImpl s3Service;

    private String fileKey;
    private String bucketName;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        bucketName = "test-bucket";
        fileKey = "test-file.txt";
        Field bucketNameField = S3ServiceImpl.class.getDeclaredField("bucketName");
        bucketNameField.setAccessible(true);
        bucketNameField.set(s3Service, "test-bucket");
    }

    @Test
    public void testPutObject() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test-file.txt");
        when(file.getBytes()).thenReturn("dummy file content".getBytes());

        String result = s3Service.putObject(file);

        assertEquals("test-file.txt", result);

        ArgumentCaptor<PutObjectRequest> captor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(s3Client).putObject(captor.capture());

        PutObjectRequest capturedRequest = captor.getValue();
        assertEquals("test-bucket", capturedRequest.getBucketName());
        assertEquals("test-file.txt", capturedRequest.getKey());
        assertNotNull(capturedRequest.getFile());
        assertTrue(capturedRequest.getFile().exists());
    }

    @Test
    public void testGetObject() throws IOException {
        S3Object s3Object = mock(S3Object.class);
        S3ObjectInputStream s3ObjectInputStream = mock(S3ObjectInputStream.class);

        when(s3ObjectInputStream.readAllBytes()).thenReturn("test file content".getBytes());
        when(s3Object.getObjectContent()).thenReturn(s3ObjectInputStream);
        when(s3Client.getObject(new GetObjectRequest(bucketName, fileKey))).thenReturn(s3Object);

        InputStream result = s3Service.getObject(fileKey);

        assertNotNull(result);
        assertEquals("test file content", new String(result.readAllBytes()));
        verify(s3Client).getObject(new GetObjectRequest(bucketName, fileKey));
    }

    @Test
    public void testDeleteObject() {
        s3Service.deleteObject(fileKey);

        verify(s3Client).deleteObject(argThat(request ->
                bucketName.equals(request.getBucketName()) && fileKey.equals(request.getKey())));
    }
}
