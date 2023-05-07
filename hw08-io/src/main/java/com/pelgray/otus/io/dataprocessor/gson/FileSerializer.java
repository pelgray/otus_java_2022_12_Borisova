package com.pelgray.otus.io.dataprocessor.gson;

import com.pelgray.otus.io.dataprocessor.Serializer;

import java.util.Map;

public class FileSerializer implements Serializer {

    public FileSerializer(String fileName) {
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл
    }
}
