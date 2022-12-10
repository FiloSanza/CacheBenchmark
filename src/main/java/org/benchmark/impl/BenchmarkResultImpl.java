package org.benchmark.impl;

import org.benchmark.BenchmarkResult;
import org.benchmark.OperationType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

public class BenchmarkResultImpl implements BenchmarkResult {
    private final List<Measurement> measurements
        = new ArrayList<>();

    public BenchmarkResultImpl() {

    }

    @Override
    public void addMeasurement(Measurement measurement) {
        this.measurements.add(measurement);
    }

    @Override
    public double getAvgExecutionTimeForOperation(OperationType operation) {
        return this.getMeasurementsAsFilteredDoubleStream(operation)
                .average()
                .getAsDouble();
    }

    @Override
    public double getMinExecutionTimeForOperation(OperationType operation) {
        return this.getMeasurementsAsFilteredDoubleStream(operation)
                .min()
                .getAsDouble();
    }

    @Override
    public double getMaxExecutionTimeForOperation(OperationType operation) {
        return this.getMeasurementsAsFilteredDoubleStream(operation)
                .max()
                .getAsDouble();
    }

    private DoubleStream getMeasurementsAsFilteredDoubleStream(OperationType operation) {
        return measurements.stream()
            .filter(measurement -> measurement.getOperationType().equals(operation))
            .mapToDouble(Measurement::getValue);
    }
}
