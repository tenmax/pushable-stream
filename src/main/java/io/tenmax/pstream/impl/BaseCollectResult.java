package io.tenmax.pstream.impl;

import io.tenmax.pstream.CollectResult;


public class BaseCollectResult<R> implements CollectResult<R> {
    protected CollectedStream<?,?,R> stream;

    void setCollectorStream(CollectedStream<?,?,R> stream) {
        this.stream = stream;
    }
    @Override
    public R snapshot() {
        return stream.snapshot();
    }
}
