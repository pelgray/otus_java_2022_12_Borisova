package com.pelgray.otus.io.dataprocessor;

import com.pelgray.otus.io.model.Measurement;

import java.util.List;
import java.util.Map;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value
        return null;
    }
}
