package org.benchmark;

public enum BenchmarkType {
    /**
     * Perform all the puts, then all the Invalidations, then all the Get
     */
    PutAllInvalidateAllGetAll,
    /**
     * Perform the put, the invalidation and the get for each object consecutively.
     */
    PutInvalidateGet,
    /**
     * Perform all the puts, then all the Get, then all the Invalidations
     */
    PutAllGetAllInvalidateAll,
    /**
     * Perform the put, the get and the invalidation for each object consecutively.
     */
    PutGetInvalidate,
    /**
     * Perform the operations in random order.
     */
    Random
}
