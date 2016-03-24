package io.tenmax.pstream;

import junit.framework.TestCase;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

public class DemoTest extends TestCase {

    public void testSimple() throws Exception{
        System.out.println("testSimple");
        PushableStream<Integer, ?> stream = PushableStream
                .<Integer>of()
                .map(i -> i*2)
                .map(i -> "test-" + i)
                .forEach(System.out::println);
        stream.push(1);
        stream.push(2);
        stream.push(3,4,5);
    }

    public void testFilter() throws Exception{
        System.out.println("testFilter");
PushableStream<Integer, ?> stream = PushableStream
        .<Integer>of()
        .filter(i -> i % 2 == 0)
        .forEach(System.out::println);

stream.push(1,2,3,4,5);
    }

    public void testCollect() throws Exception{
        System.out.println("testCollect");
        CollectResult<Long> result = CollectResult.create();
        PushableStream<Integer, ?> stream = PushableStream
                .<Integer>of()
                .collect(counting(), result);

        stream.push(1,2,3,4,5);
        System.out.println(result.snapshot());
    }

    public void testCollectMultiple() throws Exception{
        System.out.println("testCollectMultiple");

        CollectResult<Long> count = CollectResult.create();
        CollectResult<Integer> sum = CollectResult.create();
        PushableStream<Integer, ?> stream = PushableStream
                .<Integer>of()
                .collect(counting(), count)
                .collect(summingInt(i -> i.intValue()), sum);

        stream.push(1,2,3,4,5);
        System.out.println(count.snapshot());
        System.out.println(sum.snapshot());
    }

    public void testCollectParallel() throws Exception{
        System.out.println("testCollectMultiple");

        CollectResult<Long> count = CollectResult.create();
        PushableStream<Integer, ?> stream = PushableStream
                .<Integer>of()
//                .forEach((i) -> System.out.println(Thread.currentThread().getName()))
                .collect(counting(), count);

        stream.push(IntStream.rangeClosed(1, 1_000_000).boxed().parallel());

        System.out.println(count.snapshot());
    }

    public void testForkTheStream() throws Exception{
        System.out.println("testCollectMultiple");

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
    }
}
