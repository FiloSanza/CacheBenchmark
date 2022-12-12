# Cache Benchmark

This project allows you to benchmark JCache compliant services.
The current implementation supports:
- Hazelcache
- Blazingcache

The benchmark will provide the following stats for GET, PUT, INVALIDATE statements:
- MIN execution time.
- AVG execution time.
- MAX execution time.

The benchmark will also provide:
- Total time spent serializing objects.
- Total time spent deserializing objects.
- Average serialization time.
- Average deserialization time.

# How to run

Before running the benchmark you will need an hazelcache instance running, to create one quickly you can use docker:

```
docker pull hazelcast/hazelcast:5.2.1
docker run -it --network hazelcast-network --rm -e HZ_CLUSTERNAME=benchmark -p 5701:5701 hazelcast/hazelcast:5.2.1
```

In the previous command the cluster name is set to `benchmark` you can change it if you whish. 
Make sure that the cluster name and the ip address in `src\main\resources\hazelcast-client.xml` match the ones in your docker container. 

You can either clone the project and run it from IntelliJ or run the latest jar in the releases with:
```sh
java -jar [filename] [benchmark type] [N]
```
Where:
- filename is the jar file name.
- [optional] benchmark type can be one of the following:
  - random (default): Performs a random permutation of GET,PUT,INVALIDATE operations on N objects.
  - putgetinvalidate: Perform put/get/invalidate in this sequence one object at the time for N objects.
  - putinvalidateget: Perform put/invalidate/get in this sequence one object at the time for N objects.
  - putallgetallinvalidateall: Perform N put operations, N get operations and N invalidate operations.
  - putallinvalidateallgetall: Perform N put operations, N invalidate operations and N get operations.
- [optional] N: number of object that will be created during the benchmark.
  