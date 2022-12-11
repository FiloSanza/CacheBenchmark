package org.benchmark.impl;

import javax.cache.Cache;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.benchmark.Benchmark;
import org.benchmark.BenchmarkResult;

public class BenchmarkImpl<T> implements Benchmark {

    private final Map<String, Cache<String, T>> cacheProviders;
    private final Map<String, BenchmarkResult> results
            = new HashMap<>();

    private final List<Benchmark.Operation<T>> operations;

    private boolean hasCompleted = false;

    public BenchmarkImpl(final Map<String, Cache<String, T>> cacheServices, final List<Benchmark.Operation<T>> operations) {
        this.cacheProviders = Map.copyOf(cacheServices);
        this.operations = List.copyOf(operations);
    }


    @Override
    public void run() {
        for (final var entry : this.cacheProviders.entrySet()) {
            SerializationStatsImpl.newInstance();
            final var result = this.runBenchmarkOnCacheService(entry.getValue());
            result.setSerializationStats(SerializationStatsImpl.getInstance());
            this.results.put(entry.getKey(), result);
            entry.getValue().close();
        }
        
        this.hasCompleted = true;
    }

    @Override
    public boolean hasCompleted() {
        return this.hasCompleted;
    }

    @Override
    public Map<String, Cache<String, T>> getProviders() {
        return this.cacheProviders;
    }

    @Override
    public Map<String, BenchmarkResult> getResults() {
        return results;
    }

    private BenchmarkResult runBenchmarkOnCacheService(Cache<String, T> service) {
        final var result = new BenchmarkResultImpl();

        // Clear the cache to avoid skewed data from previous runs.
        service.clear();

        this.operations.forEach(op -> {
            final double executionTime = this.executeOperationAndGetExecutionTimeInSeconds(service, op);
            final var newMeasurement = BenchmarkResult.Measurement.create(op.getOperation(), executionTime);
            result.addMeasurement(newMeasurement);
        });

        return result;
    }

    private double executeOperationAndGetExecutionTimeInSeconds(Cache<String, T> service, Operation<T> operation) {
        double start = 0, end = 0;

        switch (operation.getOperation()) {
            case GET:
                start = System.currentTimeMillis();
                final var obj = service.get(operation.getKey());
                end = System.currentTimeMillis();
                break;
            case PUT:
                start = System.currentTimeMillis();
                service.put(operation.getKey(), operation.getObject());
                end = System.currentTimeMillis();
                break;
            case INVALIDATE:
                start = System.currentTimeMillis();
                service.remove(operation.getKey());
                end = System.currentTimeMillis();
                break;
        }

        return (end - start) / 1000.0;
    }
}
