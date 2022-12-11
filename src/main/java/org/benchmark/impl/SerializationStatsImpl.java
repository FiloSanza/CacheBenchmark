package org.benchmark.impl;

import org.benchmark.SerializationStats;

import java.io.Serial;

public class SerializationStatsImpl implements SerializationStats {
    private static SerializationStats instance = null;

    private double totalSerializationTimeInSeconds;
    private double totalDeserializationTimeInSeconds;
    private int totalSerializations;
    private int totalDeserializations;

    public SerializationStatsImpl() {
        this.totalSerializationTimeInSeconds = 0;
        this.totalDeserializationTimeInSeconds = 0;
        this.totalSerializations = 0;
        this.totalDeserializations = 0;
    }

    @Override
    public void registerSerialization(double executionTimeInSeconds) {
        this.totalSerializationTimeInSeconds += executionTimeInSeconds;
        this.totalSerializations++;
    }

    @Override
    public void registerDeserialization(double executionTimeInSeconds) {
        this.totalDeserializationTimeInSeconds += executionTimeInSeconds;
        this.totalDeserializations++;
    }

    @Override
    public double getTotalSerializationTimeInSeconds() {
        return this.totalSerializationTimeInSeconds;
    }

    @Override
    public double getTotalDeserializationTimeInSeconds() {
        return this.totalDeserializationTimeInSeconds;
    }

    @Override
    public double getAverageSerializationTimeInSeconds() {
        return this.totalSerializations == 0
                ? 0
                : this.totalSerializationTimeInSeconds / this.totalSerializations;
    }

    @Override
    public double getAverageDeserializationTimeInSeconds() {
        return this.totalDeserializations == 0
                ? 0
                : this.totalDeserializationTimeInSeconds / this.totalDeserializations;
    }

    @Override
    public int getNumberOfSerializations() {
        return this.totalSerializations;
    }

    @Override
    public int getNumberOfDeserializations() {
        return this.totalDeserializations;
    }

    public static SerializationStats getInstance() {
        if (SerializationStatsImpl.instance == null) {
            SerializationStatsImpl.newInstance();
        }
        return SerializationStatsImpl.instance;
    }

    public static void newInstance() {
        SerializationStatsImpl.instance = new SerializationStatsImpl();
    }
}
