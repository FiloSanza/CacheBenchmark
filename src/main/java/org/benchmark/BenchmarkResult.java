package org.benchmark;

public interface BenchmarkResult {
    /**
     * Add a new measurement to this {@link BenchmarkResult}.
     *
     * @param measurement
     */
    void addMeasurement(Measurement measurement);

    /**
     * Get the average execution time for the specified {@link OperationType operation}.
     *
     * @param operation
     * @return The average execution time as double in seconds.
     */
    double getAvgExecutionTimeForOperation(OperationType operation);

    /**
     * Get the minimum execution time for the specified {@link OperationType operation}.
     *
     * @param operation
     * @return The minimum execution time as double in seconds.
     */
    double getMinExecutionTimeForOperation(OperationType operation);

    /**
     * Get the maximum execution time for the specified {@link OperationType operation}.
     *
     * @param operation
     * @return The maximum execution time as double in seconds.
     */
    double getMaxExecutionTimeForOperation(OperationType operation);
}
