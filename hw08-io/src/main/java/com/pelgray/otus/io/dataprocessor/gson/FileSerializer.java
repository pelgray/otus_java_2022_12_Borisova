package com.pelgray.otus.io.dataprocessor.gson;

import com.google.gson.Gson;
import com.pelgray.otus.io.dataprocessor.FileProcessException;
import com.pelgray.otus.io.dataprocessor.Serializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final String fileName;

    private final Gson gson = new Gson();

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    /**
     * формирует результирующий json и сохраняет его в файл
     */
    @Override
    public void serialize(Map<String, Double> data) {
        if (fileName == null || fileName.isBlank()) {
            throw new FileProcessException("The output file must not be null or blank");
        }
        var outputFile = new File(fileName);
        try {
            Files.deleteIfExists(outputFile.toPath());
            try (var writer = gson.newJsonWriter(new FileWriter(outputFile))) {
                gson.toJson(gson.toJsonTree(data), writer);
            }
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
