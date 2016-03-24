package io.tenmax.pstream;

import junit.framework.TestCase;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PushableStreamTest extends TestCase {

    public void testOf() throws Exception {

    }

    public void testPush() throws Exception {
        AtomicInteger acc = new AtomicInteger();
        PushableStream<Integer, Integer> stream =
        PushableStream
        .<Integer>of()
        .forEach(i -> acc.addAndGet(i.intValue()));

        // array
        stream.push(1).push(2,3).push(4,5,6).push(7,8,9,10);
        assertEquals(55, acc.get());

        // stream
        acc.set(0);
        stream.push(IntStream.rangeClosed(0, 10).boxed());
        assertEquals(55, acc.get());

        // iterator
        acc.set(0);
        stream.push(IntStream.rangeClosed(0, 10).boxed().iterator());
        assertEquals(55, acc.get());
    }

    public void testFilter() throws Exception {
        AtomicInteger acc = new AtomicInteger();

        push(
            PushableStream.<Integer>of()
            .filter(i -> i % 2 == 0)
            .forEach(i -> acc.addAndGet(i.intValue()))
        );

        assertEquals(20, acc.get());
    }

    public void testMap() throws Exception {
        StringBuffer sb = new StringBuffer();

        push(
            PushableStream
            .<Integer>of()
            .map(i -> "" + i)
            .forEach(s -> sb.append(s))
        );

        assertEquals("0123456789", sb.toString());
    }

    public void testCollect() throws Exception {

        CollectResult<Integer> result = CollectResult.create();

        push(
            PushableStream
            .<Integer>of()
            .collect(Collectors.summingInt((i) -> i.intValue()), result)
        );

        assertEquals(45, result.snapshot().intValue());
    }

    public void testCollectPar() throws Exception {
        CollectResult<Integer> result = CollectResult.create();

        pushPar(
            PushableStream
            .<Integer>of()
            .collect(Collectors.summingInt((i) -> i.intValue()), result)
        );

        assertEquals(9999 * 10000 / 2, result.snapshot().intValue());
    }

    public void push(Consumer<Integer> consumer) {
        IntStream
        .range(0, 10)
        .boxed()
        .forEach(consumer);
    }

    public void pushPar(Consumer<Integer> consumer) {
        IntStream
        .range(0, 10000)
        .boxed()
        .parallel()
        .forEach(consumer);
    }

}