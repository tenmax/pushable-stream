package io.tenmax.pstream.impl;

public class SourceStream<T> extends BasePushableStream<T, T> {

    public SourceStream() {
        super();

        this.sourceStream = (t) -> {
            pushDownStream(t);
        };
    }

}
