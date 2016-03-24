package io.tenmax.pstream.impl;

import java.util.function.Predicate;

public class FilteredStream<I, O> extends BasePushableStream<I, O> {

    public FilteredStream(
            BasePushableStream<I, O> upStream,
            Predicate<O> predicate) {
        super(upStream.sourceStream);


        upStream.downStream = (O output) -> {
            if (predicate.test(output)) {
                pushDownStream(output);
            }
        };
    }
}
