package com.pelgray.otus.io.dataprocessor.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pelgray.otus.io.dataprocessor.FileProcessException;
import com.pelgray.otus.io.dataprocessor.Loader;
import com.pelgray.otus.io.model.Measurement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    /**
     * читает файл, парсит и возвращает результат
     */
    @Override
    public List<Measurement> load() {
        if (fileName == null || fileName.isBlank()) {
            throw new FileProcessException("The input file must not be null or blank");
        }
        var result = new ArrayList<Measurement>();
        try (var resource = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (resource == null) {
                throw new FileProcessException("The input file could not be found in resources directory, "
                                                       + "the input file is in a package that is not opened unconditionally, "
                                                       + "or access to the input file is denied by the security manager.");
            }
            for (var node : objectMapper.readTree(resource)) {
                result.add(getMeasurement(node));
            }
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
        return result;
    }

    private Measurement getMeasurement(JsonNode node) {
        return new Measurement(node.findValue("name").textValue(), node.findValue("value").doubleValue());
    }
}
