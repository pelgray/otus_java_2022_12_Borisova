package com.pelgray.otus.io.dataprocessor.gson;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.pelgray.otus.io.dataprocessor.FileProcessException;
import com.pelgray.otus.io.dataprocessor.Loader;
import com.pelgray.otus.io.model.Measurement;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    private final Gson gson = new Gson();

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
        var resource = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName);
        if (resource == null) {
            throw new FileProcessException("The input file could not be found in resources directory, "
                                                   + "the input file is in a package that is not opened unconditionally, "
                                                   + "or access to the input file is denied by the security manager.");
        }
        var result = new ArrayList<Measurement>();
        try (var reader = gson.newJsonReader(new InputStreamReader(resource))) {
            reader.beginArray();
            while (reader.hasNext()) {
                result.add(getMeasurement(reader));
            }
            reader.endArray();
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
        return result;
    }

    private Measurement getMeasurement(JsonReader reader) throws IOException {
        String mName = null;
        double mValue = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            var key = reader.nextName();
            if (key.equals("name")) {
                mName = reader.nextString();
            } else if (key.equals("value")) {
                mValue = reader.nextDouble();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Measurement(mName, mValue);
    }
}
