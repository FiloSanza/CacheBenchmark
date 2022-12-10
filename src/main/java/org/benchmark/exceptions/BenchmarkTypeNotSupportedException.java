package org.benchmark.exceptions;

import org.benchmark.BenchmarkType;

public class BenchmarkTypeNotSupportedException extends Exception {
    final BenchmarkType type;

    public BenchmarkTypeNotSupportedException(final BenchmarkType type) {
        this.type = type;
    }

    public BenchmarkType getUnsupportedType() {
        return this.type;
    }
}
