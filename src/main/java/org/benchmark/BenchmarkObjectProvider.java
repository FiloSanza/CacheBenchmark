package org.benchmark;

public interface BenchmarkObjectProvider<T> {
    /**
     * Create an object used in a {@link Benchmark.Operation}.
     *
     * @return the generated object.
     */
    T get();
}
