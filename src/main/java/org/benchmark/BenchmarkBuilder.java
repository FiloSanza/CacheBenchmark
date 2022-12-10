package org.benchmark;

import org.benchmark.exceptions.BenchmarkBuilderException;
import org.benchmark.exceptions.BenchmarkTypeNotSupportedException;
import org.benchmark.exceptions.ObjectProviderMissingException;
import org.benchmark.exceptions.OperationsAlreadySetException;

import javax.cache.Cache;
import java.util.List;

public interface BenchmarkBuilder<T> {
    /**
     * Add a new {@link Cache} provider object.
     *
     * @param name the name of the provider.
     * @param provider the {@link Cache} object.
     * @return this
     */
    BenchmarkBuilder addCacheProvider(final String name, final Cache<String, T> provider);

    /**
     * Sets the benchmarks operations.
     *
     * @param operations the operations that will be performed by the {@link Benchmark}.
     * @return this
     * @throws OperationsAlreadySetException if the operation array has already been created.
     */
    BenchmarkBuilder setOperations(final List<Benchmark.Operation<T>> operations)
        throws OperationsAlreadySetException;

    /**
     * Auto generated the operations performed by the benchmark.
     *
     * @param numberOfObjects the number of objects that will be stored.
     * @param type specifies how the operations will be ordered.
     * @return this
     * @throws BenchmarkTypeNotSupportedException if there is no implementation for the benchmark type passed.
     * @throws OperationsAlreadySetException if the operation array has already been created.
     */
    BenchmarkBuilder createOperations(final int numberOfObjects, BenchmarkType type)
        throws BenchmarkTypeNotSupportedException, OperationsAlreadySetException, ObjectProviderMissingException;

    /**
     * Set the object provider used to create the object for {@link Benchmark.Operation}.
     *
     * @param objectProvider
     * @return this
     */
    BenchmarkBuilder setObjectProvider(final BenchmarkObjectProvider<T> objectProvider);

    /**
     * Build the {@link Benchmark} object.
     *
     * @return the configured {@link Benchmark}.
     * @throws BenchmarkBuilderException if some fields are missing.
     */
    Benchmark build() throws BenchmarkBuilderException;
}
