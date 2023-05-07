package com.pelgray.otus.io.dataprocessor;

import com.pelgray.otus.io.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ProcessorAggregator implements Processor {
    /**
     * группирует выходящий список по name, при этом суммирует поля value
     */
    @Override
    public Map<String, Double> process(List<Measurement> data) {
        return data.stream()
                .collect(Collectors.toMap(
                        Measurement::getName,
                        Measurement::getValue,
                        Double::sum,
                        TreeMap::new
                ));
    }
}
