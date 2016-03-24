package io.tenmax.pstream.impl;


import java.util.function.Function;

public class MappedStream<I, O> extends BasePushableStream<I, O> {


    public <T> MappedStream(BasePushableStream<I, T> upStream, Function<T, O> mapper){
        super(upStream.sourceStream);

        upStream.downStream = (T item) -> {
              pushDownStream(mapper.apply(item));
        };
    }
}
