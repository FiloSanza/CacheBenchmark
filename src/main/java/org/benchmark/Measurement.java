package org.benchmark;

public interface Measurement {
    /**
     * Returns the {@link OperationType}.
     *
     * @return the {@link OperationType}.
     */
    OperationType getOperationType();

    /**
     * Returns the measurement in seconds.
     *
     * @return measurement in seconds as double.
     */
    double getValue();
}
