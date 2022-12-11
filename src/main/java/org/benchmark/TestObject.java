package org.benchmark;

import org.apache.commons.lang3.RandomStringUtils;
import org.benchmark.impl.SerializationStatsImpl;

import java.io.*;
import java.util.Random;

public class TestObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1584704831973161849L;
    private int aNumber;
    private String bigString;
    private double[] otherNumbers;

    public TestObject(final int aNumber, final String bigString, final double[] otherNumbers) {
        this.aNumber = aNumber;
        this.bigString = bigString;
        this.otherNumbers = otherNumbers;
    }

    public TestObject () {

    }

    public int getANumber() {
        return this.aNumber;
    }

    public String getBigString() {
        return this.bigString;
    }

    public double[] getOtherNumbers() {
        return this.otherNumbers;
    }

    public void getANumber(final int aNumber) {
        this.aNumber = aNumber;
    }

    public void getBigString(final String bigString) {
        this.bigString = bigString;
    }

    public void getOtherNumbers(final double[] doubles) {
        this.otherNumbers = doubles;
    }

    @Serial
    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {
        final double start = System.currentTimeMillis();
        this.aNumber = aInputStream.readInt();
        this.bigString = (String)aInputStream.readObject();
        this.otherNumbers = (double[])aInputStream.readObject();
        final double end = System.currentTimeMillis();
        SerializationStatsImpl.getInstance().registerDeserialization((end - start) / 1000.0);
    }

    @Serial
    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
        final double start = System.currentTimeMillis();
        aOutputStream.writeInt(this.aNumber);
        aOutputStream.writeObject(this.bigString);
        aOutputStream.writeObject(this.otherNumbers);
        final double end = System.currentTimeMillis();
        SerializationStatsImpl.getInstance().registerSerialization((end - start) / 1000.0);
    }

    public static TestObject createRandom(final int stringLen, final int arrayLen) {
        final var rng = new Random();
        final int number = rng.nextInt();
        final String string = RandomStringUtils.random(stringLen);
        final double[] numbers = rng.doubles(arrayLen).toArray();

        return new TestObject(number, string, numbers);
    }
}
