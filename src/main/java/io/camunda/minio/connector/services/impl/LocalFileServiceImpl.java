package io.camunda.minio.connector.services.impl;

import io.camunda.minio.connector.exceptions.LocalFileException;
import io.camunda.minio.connector.services.LocalFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileServiceImpl implements LocalFileService {

    Logger logger = LoggerFactory.getLogger(LocalFileServiceImpl.class);

    @Override
    public Path saveFile(byte[] content, String filePath) throws IOException {
        Path file = Paths.get(filePath);
        if (Files.exists(file)) {
            throw new LocalFileException(String.format("The file already exists: %s", filePath));
        }
        Path directories = Files.createDirectories(file.getParent());
        logger.info("Created directories {}", directories);
        logger.info("Writing file to {}", filePath);
        try (OutputStream stream = Files.newOutputStream(file)) {
            stream.write(content);
            logger.debug("{} bytes written to disk", content.length);
            return file;
        }
    }

    @Override
    public byte[] uploadFile(String filePath) throws IOException {
        Path file = Paths.get(filePath);
        if (!Files.exists(file)) {
            throw new LocalFileException(String.format("The file doesn't exist: %s", filePath));
        }
        logger.info("Reading file from {}", filePath);
        try (InputStream stream = Files.newInputStream(file)) {
            byte[] bytes = stream.readAllBytes();
            logger.debug("{} bytes read from disk", bytes.length);
            return bytes;
        }
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        Path file = Paths.get(filePath);
        logger.info("Deleting file {}", filePath);
        boolean deleted = Files.deleteIfExists(file);
        if (deleted) {
            logger.debug("File deleted from disk: {}", filePath);
        } else {
            logger.debug("File didn't exist: {}", filePath);
        }
    }
}
