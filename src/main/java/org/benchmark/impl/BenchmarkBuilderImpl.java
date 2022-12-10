package org.benchmark.impl;

import org.benchmark.*;
import org.benchmark.exceptions.BenchmarkBuilderException;
import org.benchmark.exceptions.BenchmarkTypeNotSupportedException;
import org.benchmark.exceptions.ObjectProviderMissingException;
import org.benchmark.exceptions.OperationsAlreadySetException;

import javax.cache.Cache;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.benchmark.OperationType.*;

public class BenchmarkBuilderImpl<T> implements BenchmarkBuilder<T> {

    private final Map<String, Cache<String, T>> cacheProviders
            = new HashMap<>();

    private final List<Benchmark.Operation<T>> operations
            = new ArrayList<>();

    private BenchmarkObjectProvider<T> objectProvider = null;

    public BenchmarkBuilderImpl() {

    }

    @Override
    public BenchmarkBuilder<T> addCacheProvider(final String name, final Cache<String, T> provider) {
        this.cacheProviders.put(name, provider);
        return this;
    }

    @Override
    public BenchmarkBuilder<T> setOperations(final List<Benchmark.Operation<T>> operations)
            throws OperationsAlreadySetException {
        if (!this.operations.isEmpty()) {
            throw new OperationsAlreadySetException();
        }
        this.operations.addAll(operations);
        return this;
    }

    @Override
    public BenchmarkBuilder setObjectProvider(BenchmarkObjectProvider<T> objectProvider) {
        this.objectProvider = objectProvider;
        return this;
    }

    @Override
    public BenchmarkBuilder<T> createOperations(final int numberOfObjects, final BenchmarkType type)
            throws BenchmarkTypeNotSupportedException, OperationsAlreadySetException, ObjectProviderMissingException {
        if (!this.operations.isEmpty()) {
            throw new OperationsAlreadySetException();
        }

        if (this.objectProvider == null) {
            throw new ObjectProviderMissingException();
        }

        switch(type) {
            case PutAllInvalidateAllGetAll:
                IntStream.range(0, numberOfObjects)
                    .mapToObj(Integer::toString)
                    .map(k -> Benchmark.Operation.create(PUT, k, objectProvider.get()))
                    .forEach(this.operations::add);
                IntStream.range(0, numberOfObjects)
                        .mapToObj(Integer::toString)
                        .map(k -> Benchmark.Operation.<T>create(INVALIDATE, k, null))
                        .forEach(this.operations::add);
                IntStream.range(0, numberOfObjects)
                        .mapToObj(Integer::toString)
                        .map(k -> Benchmark.Operation.<T>create(GET, k, null))
                        .forEach(this.operations::add);
                break;
            case PutAllGetAllInvalidateAll:
                IntStream.range(0, numberOfObjects)
                        .mapToObj(Integer::toString)
                        .map(k -> Benchmark.Operation.create(PUT, k, objectProvider.get()))
                        .forEach(this.operations::add);
                IntStream.range(0, numberOfObjects)
                        .mapToObj(Integer::toString)
                        .map(k -> Benchmark.Operation.<T>create(GET, k, null))
                        .forEach(this.operations::add);
                IntStream.range(0, numberOfObjects)
                        .mapToObj(Integer::toString)
                        .map(k -> Benchmark.Operation.<T>create(INVALIDATE, k, null))
                        .forEach(this.operations::add);
                break;
            case PutInvalidateGet:
                IntStream.range(0, numberOfObjects)
                        .mapToObj(Integer::toString)
                        .flatMap(k -> Stream.of(
                                Benchmark.Operation.create(PUT, k, objectProvider.get()),
                                Benchmark.Operation.<T>create(INVALIDATE, k, null),
                                Benchmark.Operation.<T>create(GET, k, null)
                        ))
                        .forEach(this.operations::add);
                break;
            case PutGetInvalidate:
                IntStream.range(0, numberOfObjects)
                        .mapToObj(Integer::toString)
                        .flatMap(k -> Stream.of(
                                Benchmark.Operation.create(PUT, k, objectProvider.get()),
                                Benchmark.Operation.<T>create(GET, k, null),
                                Benchmark.Operation.<T>create(INVALIDATE, k, null)
                        ))
                        .forEach(this.operations::add);
                break;
            case Random:
                IntStream.range(0, numberOfObjects)
                        .mapToObj(Integer::toString)
                        .flatMap(k -> Stream.of(
                            Benchmark.Operation.create(PUT, k, objectProvider.get()),
                            Benchmark.Operation.<T>create(GET, k, null),
                            Benchmark.Operation.<T>create(INVALIDATE, k, null)
                        ))
                    .forEach(this.operations::add);
                Collections.shuffle(this.operations);
                break;
            default:
                throw new BenchmarkTypeNotSupportedException(type);
        }

        return this;
    }

    @Override
    public Benchmark<T> build() throws BenchmarkBuilderException {
        if (this.operations.isEmpty() || this.cacheProviders.isEmpty()) {
            throw new BenchmarkBuilderException();
        }
        return new BenchmarkImpl<T>(this.cacheProviders, this.operations);
    }
}
