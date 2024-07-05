package io.camunda.minio.connector.model;

import io.camunda.connector.generator.java.annotation.TemplateProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

@Data
public class AuthenticationRequest {

  @NotEmpty
  @TemplateProperty(group = "authenticationRequest")
  @ToString.Exclude
  private String endPoint;

  @NotEmpty
  @TemplateProperty(group = "authenticationRequest")
  @ToString.Exclude
  private String accessKey;

  @NotEmpty
  @TemplateProperty(group = "authenticationRequest")
  @ToString.Exclude
  private String secretKey;
}
