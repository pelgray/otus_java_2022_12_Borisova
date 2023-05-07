package com.pelgray.otus.io.dataprocessor.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pelgray.otus.io.dataprocessor.FileProcessException;
import com.pelgray.otus.io.dataprocessor.Serializer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final String fileName;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    /**
     * формирует результирующий json и сохраняет его в файл
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void serialize(Map<String, Double> data) {
        if (fileName == null || fileName.isBlank()) {
            throw new FileProcessException("The output file must not be null or blank");
        }
        var outputFile = new File(fileName);
        try {
            Files.deleteIfExists(outputFile.toPath());
            outputFile.createNewFile();
            objectMapper.writeValue(outputFile, data);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
