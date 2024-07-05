package io.camunda.minio.connector;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import io.camunda.connector.generator.java.annotation.ElementTemplate;
import io.camunda.minio.connector.model.ConnectorRequest;
import io.camunda.minio.connector.services.CommonFileService;
import io.camunda.minio.connector.services.impl.CommonFileServiceImpl;
import io.camunda.minio.connector.model.enums.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@OutboundConnector(
    name = "MinIO Connector",
    inputVariables = {"authenticationRequest", "requestDetails"},
    type = "io.camunda:template:1")
@ElementTemplate(
    id = "io.camunda.connector.Template.v1",
    name = "MinIO Connector",
    version = 1,
    description = "MinIO Connector",
    propertyGroups = {
      @ElementTemplate.PropertyGroup(id = "authenticationRequest", label = "Authentication"),
      @ElementTemplate.PropertyGroup(id = "requestDetails", label = "RequestDetails")
    },
    icon = "icon.svg",
    inputDataClass = ConnectorRequest.class)
public class MyConnectorFunction implements OutboundConnectorFunction {

  private static final Logger LOGGER = LoggerFactory.getLogger(MyConnectorFunction.class);

  private final CommonFileService fileService = new CommonFileServiceImpl();

  @Override
  public Object execute(OutboundConnectorContext context) throws IOException {
    ConnectorRequest request = context.bindVariables(ConnectorRequest.class);
    LOGGER.info("Executing MinIO connector with request {}", request);
    return executeConnector(request);
  }

  private Object executeConnector(final ConnectorRequest connectorRequest) throws IOException {
    return switch (connectorRequest.getRequestDetails().getOperationType()) {
      case OperationType.DELETE_OBJECT -> fileService.deleteFile(connectorRequest);
      case OperationType.PUT_OBJECT -> fileService.uploadFile(connectorRequest);
      case OperationType.GET_OBJECT -> fileService.downloadFile(connectorRequest);
    };
  }
}
