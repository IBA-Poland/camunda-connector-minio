package io.camunda.minio.connector.services;

import io.camunda.minio.connector.model.ConnectorRequest;

import java.io.IOException;

public interface CommonFileService {

    Object uploadFile(ConnectorRequest request) throws IOException;
    Object deleteFile(ConnectorRequest request) throws IOException;
    Object downloadFile(ConnectorRequest request) throws IOException;
}
