package io.camunda.minio.connector.services.impl;


import io.camunda.minio.connector.model.localFile.FileContent;
import io.camunda.minio.connector.model.ConnectorRequest;
import io.camunda.minio.connector.services.CommonFileService;
import io.camunda.minio.connector.services.LocalFileService;
import io.camunda.minio.connector.services.MinioFileService;

import javax.activation.MimetypesFileTypeMap;
import java.io.IOException;

public class CommonFileServiceImpl implements CommonFileService {

    private final MinioFileService minioFileService = new MinioFileServiceImpl();
    private final LocalFileService localFileService = new LocalFileServiceImpl();


    @Override
    public Object uploadFile(ConnectorRequest request) throws IOException {
/*        String contentType = Objects.requireNonNull(request.getRequestDetails().getContentType(),
                "Content type must be set");*/
        String contentType =  convertToContentType(request.getRequestDetails().getFileName());

        byte[] content = localFileService.uploadFile(request.getRequestDetails().getFilePath());
        minioFileService.putObject(request, FileContent.builder()
                .content(content)
                .contentLength((long) content.length)
                .contentType(contentType)
                .build()
        );
        return request;
    }

    @Override
    public Object deleteFile(ConnectorRequest request) throws IOException {
        minioFileService.removeObject(request);
        return request;
    }

    @Override
    public Object downloadFile(ConnectorRequest request) throws IOException {
        FileContent response = minioFileService.getObject(request);
        localFileService.saveFile(response.getContent(), request.getRequestDetails().getFilePath());
        return request;
    }


    private String convertToContentType(String fileName ){
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        return mimetypesFileTypeMap.getContentType(fileName);
    }
}
