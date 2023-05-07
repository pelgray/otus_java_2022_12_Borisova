package com.pelgray.otus.io.dataprocessor;

import com.pelgray.otus.io.model.Measurement;

import java.util.List;
import java.util.Map;

public interface Processor {

    Map<String, Double> process(List<Measurement> data);
}
