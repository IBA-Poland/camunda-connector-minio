package io.camunda.minio.connector.model;

import io.camunda.connector.generator.java.annotation.TemplateProperty;
import io.camunda.minio.connector.model.enums.OperationType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestDetails {

  @NotEmpty
  @TemplateProperty(group = "requestDetails")
  private String bucketName;

  @TemplateProperty(group = "requestDetails")
  private String fileName;

  @TemplateProperty(group = "requestDetails")
  private String filePath;

  @NotNull
  @TemplateProperty(group = "requestDetails")
  private OperationType operationType;
}
