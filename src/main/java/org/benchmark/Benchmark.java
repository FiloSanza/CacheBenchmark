package org.benchmark;

import javax.annotation.Nullable;
import javax.cache.Cache;
import java.util.Map;

public interface Benchmark<T> {

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
     * @return a map with the cache providers.
     */
    Map<String, Cache<String, T>> getProviders();

    /**
     * Returns the benchmark results as a {@link Map}.
     * The key is the string label associated to the cache provider, the value is a {@link BenchmarkResult} object.
     *
     * @return a {@link Map} containing {@link BenchmarkResult} for each {@link Cache Cache} provider instance.
     */
    Map<String, BenchmarkResult> getResults();

    /**
     * Class used to represent the structure of the operations used by {@link Benchmark}.
     *
     * @param <T> type of object that will be used by {@link Benchmark}.
     */
    class Operation<T> {
        private final OperationType operation;
        private final String key;
        private final T object;

        private Operation(final OperationType operation, final String key, @Nullable final T object) {
            this.operation = operation;
            this.key = key;
            this.object = object;
        }

        /**
         * Get the {@link OperationType}.
         * @return the {@link OperationType}.
         */
        public OperationType getOperation() {
            return operation;
        }

        /**
         * Get the key used by this operation.
         * @return the key as {@link String}.
         */
        public String getKey() {
            return key;
        }

        /**
         * Get the object.
         * @return the object used by the operation.
         */
        public T getObject() {
            return object;
        }

        /**
         * Create a new {@link Operation}.
         *
         * @param operation type of the operation.
         * @param object object that will be eventually stored, if the operation isn't PUT the object can be null.
         * @return A new {@link Operation} object.
         * @param <T> Type of the object put/retrieved/invalidated by the operation.
         */
        public static <T> Operation<T> create(final OperationType operation, final String key, @Nullable final T object) {
            return new Operation<>(operation, key, object);
        }
    }
}
