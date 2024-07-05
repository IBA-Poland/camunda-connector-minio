package io.camunda.minio.connector.services;

import io.camunda.minio.connector.model.localFile.FileContent;
import io.camunda.minio.connector.model.ConnectorRequest;

import java.io.IOException;

public interface MinioFileService {

    void removeObject(ConnectorRequest requestData);
    void putObject(ConnectorRequest requestData, FileContent fileContent) throws IOException;
    FileContent getObject(ConnectorRequest requestData) throws IOException;
}
