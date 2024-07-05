package io.camunda.minio.connector.model.localFile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileContent {

    private byte[] content;
    private String contentType;
    private Long contentLength;
}
