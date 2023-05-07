package com.pelgray.otus.io.dataprocessor;

import com.pelgray.otus.io.model.Measurement;

import java.util.List;

public interface Loader {

    List<Measurement> load();
}
