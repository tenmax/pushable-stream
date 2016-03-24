package io.tenmax.pstream.impl;

import io.tenmax.pstream.CollectResult;
import io.tenmax.pstream.PushableStream;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

public abstract  class BasePushableStream<I, O> implements PushableStream<I, O> {
    protected Consumer<I> sourceStream;
    protected Consumer<O> downStream;

    protected BasePushableStream() {
    }

    protected BasePushableStream(Consumer<I> sourceStream) {
        this.sourceStream = sourceStream;
    }

    @Override
    public PushableStream<I, O> filter(Predicate<O> predicate) {
        return new FilteredStream<>(this, predicate);
    }

    @Override
    public <R> PushableStream<I, R> map(Function<O, R> mapper) {
        return new MappedStream<>(this, mapper);
    }

    @Override
    public PushableStream<I, O> forEach(Consumer<O> consumer) {
        return new ForEachStream<>(this, consumer);
    }

    @Override
    public <R, A> PushableStream<I, O> collect(Collector<O, A, R> collector, CollectResult<R> result) {
        CollectedStream<I, O, R> stream = new CollectedStream<>(this, collector);
        if (!(result instanceof BaseCollectResult)) {
            throw new IllegalArgumentException("wrong collect result.");
        }

        ((BaseCollectResult)result).setCollectorStream(stream);
        return stream;
    }

    public PushableStream<I, O> pushOne(I input) {
        sourceStream.accept(input);
        return this;
    }

    @Override
    public PushableStream<I, O> push(I... items) {
        for (I item: items){
            pushOne(item);
        }

        return this;
    }

    @Override
    public PushableStream<I, O> push(Stream<I> stream) {
        stream.forEach((item) -> pushOne(item));

        return this;
    }

    @Override
    public PushableStream<I, O> push(Iterator<I> iterator) {
        while (iterator.hasNext()) {
            pushOne(iterator.next());
        }

        return this;
    }

    protected final void pushDownStream(O output) {
        if (downStream != null) {
            downStream.accept(output);
        }
    }



    @Override
    public void accept(I i) {
        sourceStream.accept(i);
    }
}
