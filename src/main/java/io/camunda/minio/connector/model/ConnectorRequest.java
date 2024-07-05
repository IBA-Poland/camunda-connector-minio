package io.camunda.minio.connector.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConnectorRequest {

  @Valid
  @NotNull
  private AuthenticationRequest authenticationRequest;

  @Valid
  @NotNull
  private RequestDetails requestDetails;
}
