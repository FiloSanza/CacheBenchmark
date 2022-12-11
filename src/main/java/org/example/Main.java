package org.example;

import blazingcache.jcache.BlazingCacheProvider;
import com.hazelcast.cache.HazelcastCachingProvider;
import de.vandermeer.asciitable.AsciiTable;
import org.benchmark.BenchmarkResult;
import org.benchmark.BenchmarkType;
import org.benchmark.OperationType;
import org.benchmark.TestObject;
import org.benchmark.impl.BenchmarkBuilderImpl;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class Main {
    private static int DEFAULT_N_OBJECTS = 1000;
    private static BenchmarkType DEFAULT_BENCHMARK_TYPE = BenchmarkType.Random;
    private static final String HAZELCACHE = "hazelcache";
    private static final String BLAZINGCACHE = "blazingcache";

    public static void main(String[] args) throws Exception {
        int numberOfObjects = DEFAULT_N_OBJECTS;
        BenchmarkType type = DEFAULT_BENCHMARK_TYPE;

        if (args.length > 0) {
            type = parseBenchmarkType(args[0]);
        }

        if (args.length > 1) {
            try {
                numberOfObjects = Integer.parseInt(args[1]);
            } catch (Exception ex) {
                System.out.println("Invalid value for number of object, default " + DEFAULT_N_OBJECTS + " used.");
            }
        }

        System.setProperty("hazelcast.jcache.provider.type", "client");
        final var benchmarkBuilder = new BenchmarkBuilderImpl<TestObject>();
        final var benchmark = benchmarkBuilder
                .addCacheProvider(HAZELCACHE, getCacheProvider(HazelcastCachingProvider.class.getName(), HAZELCACHE + "-benchmark"))
                .addCacheProvider(BLAZINGCACHE, getCacheProvider(BlazingCacheProvider.class.getName(), BLAZINGCACHE + "-benchmark"))
                .setObjectProvider(() -> TestObject.createRandom((1 << 10), (1 << 10)))
                .createOperations(numberOfObjects, type)
                .build();

        benchmark.run();

        final Map<String, BenchmarkResult> results = benchmark.getResults();
        printResult(results);
    }

    private static void printResult(final Map<String, BenchmarkResult> results) {
        final var table = new AsciiTable();
        final var keys = results.keySet().stream().sorted().toList();

        // Create header
        table.addRule();
        final var header = new ArrayList<String>();
        header.add("X");
        header.addAll(keys);
        table.addRow(header);
        table.addRule();

        // Create results rows
        table.addRow(getTableRow("AVG GET", keys, results, b -> b.getAvgExecutionTimeForOperation(OperationType.GET)));
        table.addRule();
        table.addRow(getTableRow("MIN GET", keys, results, b -> b.getMinExecutionTimeForOperation(OperationType.GET)));
        table.addRule();
        table.addRow(getTableRow("MAX GET", keys, results, b -> b.getMaxExecutionTimeForOperation(OperationType.GET)));
        table.addRule();
        table.addRow(getTableRow("AVG PUT", keys, results, b -> b.getAvgExecutionTimeForOperation(OperationType.PUT)));
        table.addRule();
        table.addRow(getTableRow("MIN PUT", keys, results, b -> b.getMinExecutionTimeForOperation(OperationType.PUT)));
        table.addRule();
        table.addRow(getTableRow("MAX PUT", keys, results, b -> b.getMaxExecutionTimeForOperation(OperationType.PUT)));
        table.addRule();
        table.addRow(getTableRow("AVG INVALIDATE", keys, results, b -> b.getAvgExecutionTimeForOperation(OperationType.INVALIDATE)));
        table.addRule();
        table.addRow(getTableRow("MIN INVALIDATE", keys, results, b -> b.getMinExecutionTimeForOperation(OperationType.INVALIDATE)));
        table.addRule();
        table.addRow(getTableRow("MAX INVALIDATE", keys, results, b -> b.getMaxExecutionTimeForOperation(OperationType.INVALIDATE)));
        table.addRule();
        table.addRow(getTableRow("TOTAL SERIALIZATION TIME", keys, results, b -> b.getSerializationStats().getTotalSerializationTimeInSeconds()));
        table.addRule();
        table.addRow(getTableRow("TOTAL DESERIALIZATION TIME", keys, results, b -> b.getSerializationStats().getTotalDeserializationTimeInSeconds()));
        table.addRule();
        table.addRow(getTableRow("AVG SERIALIZATION TIME", keys, results, b -> b.getSerializationStats().getAverageSerializationTimeInSeconds()));
        table.addRule();
        table.addRow(getTableRow("AVG DESERIALIZATION TIME", keys, results, b -> b.getSerializationStats().getAverageDeserializationTimeInSeconds()));
        table.addRule();
        table.addRow(getTableRow("TOTAL SERIALIZATIONS", keys, results, b -> b.getSerializationStats().getNumberOfSerializations()));
        table.addRule();
        table.addRow(getTableRow("TOTAL DESERIALIZATIONS", keys, results, b -> b.getSerializationStats().getNumberOfDeserializations()));
        table.addRule();

        System.out.println(table.render());
    }

    private static <T extends Number> List<String> getTableRow(
            final String title,
            final List<String> orderedKeys,
            final Map<String, BenchmarkResult> results,
            final Function<BenchmarkResult, T> func
    ) {
        return Stream.concat(
            Stream.of(title),
            orderedKeys.stream()
                .map(results::get)
                .map(func)
                .map(Object::toString)
        ).toList();
    }

    private static <T extends Serializable> Cache<String, T> getCacheProvider(final String providerClassName, final String cacheName) {
        final var provider = Caching.getCachingProvider(providerClassName);
        final var manager = provider.getCacheManager();
        final var configuration = new MutableConfiguration<String, T>();
        configuration.setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.FIVE_MINUTES));
        return manager.createCache(cacheName, configuration);
    }

    private static BenchmarkType parseBenchmarkType(String value) {
        return switch (value) {
            case "putallgetallinvalidateall" -> BenchmarkType.PutAllGetAllInvalidateAll;
            case "putallinvalidateallgetall" -> BenchmarkType.PutAllInvalidateAllGetAll;
            case "putinvalidateget" -> BenchmarkType.PutInvalidateGet;
            case "putgetinvalidate" -> BenchmarkType.PutGetInvalidate;
            default -> BenchmarkType.Random;
        };
    }
}