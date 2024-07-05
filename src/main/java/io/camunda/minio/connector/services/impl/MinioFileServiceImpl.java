package io.camunda.minio.connector.services.impl;

import io.camunda.minio.connector.model.AuthenticationRequest;
import io.camunda.minio.connector.model.ConnectorRequest;
import io.camunda.minio.connector.model.localFile.FileContent;
import io.camunda.minio.connector.exceptions.MinioFileException;
import io.camunda.minio.connector.services.MinioFileService;
import io.minio.*;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javax.activation.MimetypesFileTypeMap;

public class MinioFileServiceImpl implements MinioFileService {

    private static final Logger logger = LoggerFactory.getLogger(MinioFileServiceImpl.class);

    @Override
    public void removeObject(ConnectorRequest requestData) throws MinioFileException {
        MinioClient minioClient = createClient(requestData.getAuthenticationRequest());
        checkBucket(minioClient,requestData.getRequestDetails().getBucketName());
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(requestData.getRequestDetails().getBucketName()).
                            object(requestData.getRequestDetails().getFileName()).build());
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error(e.getMessage());
            throw new MinioFileException(String.format("Minio exception: %s", e.getMessage()));
        }
        logger.info(requestData.getRequestDetails().getFileName() + " has been removed from the bucket.");
    }

    @Override
    public void putObject(ConnectorRequest requestData, FileContent fileContent) {
        MinioClient minioClient = createClient(requestData.getAuthenticationRequest());
        checkBucket(minioClient,requestData.getRequestDetails().getBucketName());
        String contentType =  convertToContentType(requestData.getRequestDetails().getFileName());
        try {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(requestData.getRequestDetails().getBucketName())
                            .object(requestData.getRequestDetails().getFileName()).
                            stream(new ByteArrayInputStream(fileContent.getContent()), fileContent.getContentLength(), -1)
                            .contentType(contentType)
                            .build());
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error(e.getMessage());
            throw new MinioFileException(String.format("Minio exception: %s", e.getMessage()));
        }
        logger.info(requestData.getRequestDetails().getFileName() + " is loaded into the bucket.");
    }

    @Override
    public FileContent getObject(ConnectorRequest requestData) throws IOException {
        FileContent fileContent;
        MinioClient minioClient = createClient(requestData.getAuthenticationRequest());
        checkBucket(minioClient,requestData.getRequestDetails().getBucketName());
        try {
            try (GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(requestData.getRequestDetails().getBucketName())
                            .object(requestData.getRequestDetails().getFileName())
                            .build())) {
                 fileContent =  FileContent.builder().content(response.readAllBytes())
                         .contentLength(Long.valueOf(Objects.requireNonNull(response.headers().get("Content-Length"))))
                         .contentType(response.headers().get("Content-Type"))
                         .build();
            }
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error(e.getMessage());
            throw new MinioFileException(String.format("Minio exception: %s", e.getMessage()));
        }
        logger.info(requestData.getRequestDetails().getFileName() + " is unloaded from the bucket.");
        return fileContent;
    }

    private MinioClient createClient (AuthenticationRequest authentication){
        return MinioClient.builder()
                        .endpoint(authentication.getEndPoint())
                        .credentials(authentication.getAccessKey(),
                                authentication.getSecretKey())
                        .build();
    }

    private void checkBucket(MinioClient minioClient, String bucketName) throws MinioFileException {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (found) {
                logger.info(bucketName + " bucket exists.");
            } else {
                logger.info(bucketName + " bucket does not exist.");
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            throw new MinioFileException(String.format("MinIO: the problem when checking the bucket: %s",
                    e.getMessage()));
        }
    }

    private String convertToContentType(String fileName ){
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        return mimetypesFileTypeMap.getContentType(fileName);
    }
}
