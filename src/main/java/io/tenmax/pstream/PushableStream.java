package io.tenmax.pstream;

import io.tenmax.pstream.impl.SourceStream;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

public interface PushableStream<I, O> extends Consumer<I> {

    static <T> PushableStream<T,T> of() {
        return new SourceStream<>();
    }

    PushableStream<I, O> filter(Predicate<O> predicate);

    <R> PushableStream<I, R> map(Function<O, R> mapper);

    PushableStream<I, O> forEach(Consumer<O> consumer);

    <R, A> PushableStream<I, O> collect(
            Collector<O, A, R> collector,
            CollectResult<R> result);

    PushableStream<I, O> push(I... items);

    PushableStream<I, O> push(Stream<I> stream);

    PushableStream<I, O> push(Iterator<I> iterator);


}
