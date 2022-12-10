package org.benchmark.impl;

import javax.cache.Cache;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.benchmark.Benchmark;
import org.benchmark.BenchmarkResult;

public class BenchmarkImpl<T> implements Benchmark {

    private final Map<String, Cache> cacheProviders;
    private final Map<String, BenchmarkResult> results
            = new HashMap<>();

    private final List<Benchmark.Operation<T>> operations;

    private boolean hasCompleted = false;

    public BenchmarkImpl(final Map<String, Cache> cacheServices, final List<Benchmark.Operation<T>> operations) {
        this.cacheProviders = Map.copyOf(cacheServices);
        this.operations = List.copyOf(operations);
    }


    @Override
    public void run() {
        for (final var entry : this.cacheProviders.entrySet()) {
            final var result = this.runBenchmarkOnCacheService(entry.getValue());
            this.results.put(entry.getKey(), result);
        }
        
        this.hasCompleted = true;
    }

    @Override
    public boolean hasCompleted() {
        return this.hasCompleted;
    }

    @Override
    public Map<String, Cache> getProviders() {
        return this.cacheProviders;
    }

    @Override
    public Map<String, BenchmarkResult> getResults() {
        return results;
    }

    private BenchmarkResult runBenchmarkOnCacheService(Cache service) {
        final var result = new BenchmarkResultImpl();

        this.operations.forEach(op -> {
            final double executionTime = this.executeOperationAndGetExecutionTime(service, op);
            result.addMeasurement(BenchmarkResult.Measurement.create(op.getOperation(), executionTime));
        });

        return result;
    }

    private double executeOperationAndGetExecutionTime(Cache service, Operation<T> operation) {
        return 0.0;
    }
}
