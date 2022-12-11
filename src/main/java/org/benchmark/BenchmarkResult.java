package org.benchmark;

import org.benchmark.impl.SerializationStatsImpl;

import java.io.Serial;

public interface BenchmarkResult {
    /**
     * Add a new measurement to this {@link BenchmarkResult}.
     *
     * @param measurement
     */
    void addMeasurement(Measurement measurement);

    /**
     * Set the serialization stats for this result.
     *
     * @param stats the serialization stats.
     */
    void setSerializationStats(SerializationStats stats);

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

    /**
     * Return the serialization stats.
     *
     * @return the stats.
     */
    SerializationStats getSerializationStats();

    /**
     * Represent a new measurement for the benchmark.
     */
    class Measurement {
        private final OperationType operation;
        private final double value;

        private Measurement(final OperationType operation, final double value) {
            this.operation = operation;
            this.value = value;
        }

        /**
         * Get the {@link OperationType}.
         * @return the operation type.
         */
        public OperationType getOperationType() {
            return operation;
        }

        /**
         * Get the measured seconds as double.
         * @return measured seconds.
         */
        public double getValue() {
            return value;
        }

        public static Measurement create(final OperationType operation, final double value) {
            return new Measurement(operation, value);
        }
    }
}
