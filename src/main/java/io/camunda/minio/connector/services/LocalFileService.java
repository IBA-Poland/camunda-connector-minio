package io.camunda.minio.connector.services;

import java.io.IOException;
import java.nio.file.Path;

public interface LocalFileService {

    Path saveFile(byte[] content, String filePath) throws IOException;
    byte[] uploadFile(String filePath) throws IOException;
    void deleteFile(String filePath) throws IOException;
}
