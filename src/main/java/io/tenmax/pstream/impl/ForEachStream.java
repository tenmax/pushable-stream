package io.tenmax.pstream.impl;

import java.util.function.Consumer;

public class ForEachStream<I, O> extends BasePushableStream<I, O>{
    private Consumer<O> consumer;

    public ForEachStream(
        BasePushableStream<I, O> upStream,
        Consumer<O> consumer)
    {
        super(upStream.sourceStream);
        this.consumer = consumer;

        upStream.downStream = (O output) -> {
            consumer.accept(output);
            pushDownStream(output);
        };
    }
}
