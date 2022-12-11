package org.benchmark;

/**
 * Models a class used to keep track of serialization stats.
 */
public interface SerializationStats {
    /**
     * Register a new serialization.
     * Increments the number of serializations performed and adds the time spend.
     *
     * @param executionTimeInSeconds time spent serializing the object.
     */
    void registerSerialization(double executionTimeInSeconds);

    /**
     * Register a new deserialization.
     * Increments the number of deserializations performed and adds the time spend.
     *
     * @param executionTimeInSeconds time spent deserializing the object.
     */
    void registerDeserialization(double executionTimeInSeconds);

    /**
     * Return the time spent serializing objects.
     *
     * @return the time passed in seconds.
     */
    double getTotalSerializationTimeInSeconds();

    /**
     * Return the time spent deserializing objects
     *
     * @return the time passed in seconds.
     */
    double getTotalDeserializationTimeInSeconds();

    /**
     * Return the average time spent serializing objects.
     *
     * @return the time passed in seconds.
     */
    double getAverageSerializationTimeInSeconds();

    /**
     * Return the average time spent deserializing objects
     *
     * @return the time passed in seconds.
     */
    double getAverageDeserializationTimeInSeconds();

    /**
     * Return the number of serializations performed.
     *
     * @return the number of serializations performed.
     */
    int getNumberOfSerializations();

    /**
     * Return the number of deserializations performed.
     *
     * @return the number of deserializations performed.
     */
    int getNumberOfDeserializations();
}
