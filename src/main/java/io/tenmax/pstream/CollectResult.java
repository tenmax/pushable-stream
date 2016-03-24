package io.tenmax.pstream;

import io.tenmax.pstream.impl.BaseCollectResult;

/**
 * Created by popcorny on 3/24/16.
 */
public interface CollectResult<R> {

    static <T> CollectResult<T> create() {
        return new BaseCollectResult<>();
    }

    R snapshot();
}
