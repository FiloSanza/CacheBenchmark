package org.example;

import org.apache.commons.lang3.RandomStringUtils;
import org.benchmark.BenchmarkResult;
import org.benchmark.BenchmarkType;
import org.benchmark.OperationType;
import org.benchmark.impl.BenchmarkBuilderImpl;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import java.util.Map;

public class Main {
    private static final String HAZELCACHE = "hazelcache";

    public static void main(String[] args) throws Exception {
        final var benchmarkBuilder = new BenchmarkBuilderImpl<String>();
        final var benchmark = benchmarkBuilder.addCacheProvider(HAZELCACHE, getHazelcacheProvider())
                .setObjectProvider(() -> RandomStringUtils.random(1 << 5))
                .createOperations(1000000, BenchmarkType.Random)
                .build();

        System.out.println("START BENCHMARK");
        benchmark.run();
        System.out.println("END BENCHMARK");

        final Map<String, BenchmarkResult> results = benchmark.getResults();
        final var hcache = results.get(HAZELCACHE);

        System.out.println("AVERAGE TIME FOR PUT: " + hcache.getAvgExecutionTimeForOperation(OperationType.PUT));
        System.out.println("AVERAGE TIME FOR INVALIDATE: " + hcache.getAvgExecutionTimeForOperation(OperationType.INVALIDATE));
        System.out.println("AVERAGE TIME FOR GET: " + hcache.getAvgExecutionTimeForOperation(OperationType.GET));

        System.out.println("MIN TIME FOR PUT: " + hcache.getMinExecutionTimeForOperation(OperationType.PUT));
        System.out.println("MIN TIME FOR INVALIDATE: " + hcache.getMinExecutionTimeForOperation(OperationType.INVALIDATE));
        System.out.println("MIN TIME FOR GET: " + hcache.getMinExecutionTimeForOperation(OperationType.GET));

        System.out.println("MAX TIME FOR PUT: " + hcache.getMaxExecutionTimeForOperation(OperationType.PUT));
        System.out.println("MAX TIME FOR INVALIDATE: " + hcache.getMaxExecutionTimeForOperation(OperationType.INVALIDATE));
        System.out.println("MAX TIME FOR GET: " + hcache.getMaxExecutionTimeForOperation(OperationType.GET));
    }

    private static Cache<String, String> getHazelcacheProvider() {
        System.setProperty("hazelcast.jcache.provider.type", "client");
        final var manager = Caching.getCachingProvider().getCacheManager();
        final var configuration = new MutableConfiguration<String, String>();
        return manager.createCache("hazelcache-benchmark", configuration);
    }
}