# Introduction

Java Stream is a great reactive library. However, it is a pull-only reactive library. In some scenario, it is desirable to behave as "push" mode. This library implement the push-version of java stream library.

# Usage
I will put the library to JCenter repo sooooooon....

# Examples

## Mapper
A mapper allows you to transform input to another format. Similar to [java.util.Stream#map](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#map-java.util.function.Function-)

``` java
PushableStream<Integer, ?> stream = PushableStream
    .<Integer>of()
    .map(i -> i*2)
    .map(i -> "test-" + i)
    .forEach(System.out::println);
    
stream.push(1);
stream.push(2);
stream.push(3,4,5);
```
        
Result:

```
test-2
test-4
test-6
test-8
test-10
```        

## Filter

A filter allows you keep the matched item. Similar to [java.util.Stream#filter](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#filter-java.util.function.Predicate-)

``` java
PushableStream<Integer, ?> stream = PushableStream
        .<Integer>of()
        .filter(i -> i % 2 == 0)
        .forEach(System.out::println);

stream.push(1,2,3,4,5);
```

Result:

```
2
4
```        

## Collector

A collector allows you reduce the items to a compact result. Similar to [java.util.Stream#collect](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#collect-java.util.stream.Collector-)


``` java
CollectResult<Long> result = CollectResult.create();
PushableStream<Integer, ?> stream = PushableStream
        .<Integer>of()
        .collect(Collectors.counting(), result);

stream.push(1,2,3,4,5);
System.out.println(result.snapshot());
```

Result:
```
15
```        


## Multiple Collector
In java stream library, there could be only one collector result. But in pushable stream. We can have multiple collectors in one stream.

```
CollectResult<Long> count = CollectResult.create();
CollectResult<Integer> sum = CollectResult.create();
PushableStream<Integer, ?> stream = PushableStream
        .<Integer>of()
        .collect(counting(), count)
        .collect(summingInt(i -> i.intValue()), sum);

stream.push(1,2,3,4,5);
System.out.println(count.snapshot());
System.out.println(sum.snapshot());
```

Result:
```
5
15
```

## Multi-Threaded Collector

Just like java stream. We support parallel execution to gain better performan

``` java
CollectResult<Long> count = CollectResult.create();
PushableStream<Integer, ?> stream = PushableStream
        .<Integer>of()
//        .forEach((i) -> System.out.println(Thread.currentThread().getName()))
        .collect(counting(), count);

stream.push(IntStream.rangeClosed(1, 1_000_000).boxed().parallel());

System.out.println(count.snapshot());
```
Result:
```
1000000
```

## Fork the stream!
Sometimes, in the ETL path, the cost of pulling or transforming data may be expensive. Pushable stream allows you to reuse the stream for multiple purposes. For example, you can print the result to the console, save the result to file, and aggregate the data at the same time!

``` java
IntStream
.range(0, 5)
.boxed()
.peek(PushableStream
    .<Integer>of()
    .forEach(System.out::println))
.peek(PushableStream
    .<Integer>of().map(i->i*2)
    .forEach(System.out::println))
.map(i -> i * 3)
.forEach(System.out::println);
```

Result:

```
0
0
0
1
2
3
2
4
6
3
6
9
4
8
12
```