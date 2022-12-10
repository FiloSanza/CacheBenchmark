package org.benchmark;

import javax.cache.Cache;
import java.util.Map;

public interface Benchmark {

    /**
     * Runs the benchmark and marks it as completed once it ends.
     */
    void run();

    /**
     * Returns the status of the benchmark.
     *
     * @return true if completed, false otherwise.
     */
    boolean hasCompleted();

    /**
     * Returns a {@link Map} where the key is a String that identifies a {@link Cache} provider instance.
     *
     * @return
     */
    Map<String, Cache> getProviders();

    /**
     * Returns the benchmark results as a {@link Map}.
     * The key is the string label associated to the cache provider, the value is a {@link BenchmarkResult} object.
     *
     * @return a {@link Map} contaning {@link BenchmarkResult} for each {@link Cache Cache} provider instance.
     */
    Map<String, BenchmarkResult> getResults();
}
