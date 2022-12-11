package org.benchmark.impl;

import org.benchmark.BenchmarkResult;
import org.benchmark.OperationType;
import org.benchmark.SerializationStats;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

public class BenchmarkResultImpl implements BenchmarkResult {
    private final List<Measurement> measurements
        = new ArrayList<>();

    private SerializationStats serializationStats = null;

    public BenchmarkResultImpl() {

    }

    @Override
    public void addMeasurement(Measurement measurement) {
        this.measurements.add(measurement);
    }

    @Override
    public void setSerializationStats(SerializationStats stats) {
        this.serializationStats = stats;
    }

    @Override
    public double getAvgExecutionTimeForOperation(OperationType operation) {
        return this.getMeasurementsAsFilteredDoubleStream(operation)
                .average()
                .orElse(0.0);
    }

    @Override
    public double getMinExecutionTimeForOperation(OperationType operation) {
        return this.getMeasurementsAsFilteredDoubleStream(operation)
                .min()
                .orElse(Double.MAX_VALUE);
    }

    @Override
    public double getMaxExecutionTimeForOperation(OperationType operation) {
        return this.getMeasurementsAsFilteredDoubleStream(operation)
                .max()
                .orElse(Double.MIN_VALUE);
    }

    @Override
    public SerializationStats getSerializationStats() {
        return this.serializationStats;
    }

    private DoubleStream getMeasurementsAsFilteredDoubleStream(OperationType operation) {
        return measurements.stream()
            .filter(measurement -> measurement.getOperationType().equals(operation))
            .mapToDouble(Measurement::getValue);
    }
}
