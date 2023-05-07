package com.pelgray.otus.io.dataprocessor.jackson;

import com.pelgray.otus.io.dataprocessor.Loader;
import com.pelgray.otus.io.model.Measurement;

import java.util.List;

public class ResourcesFileLoader implements Loader {

    public ResourcesFileLoader(String fileName) {
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        return null;
    }
}
