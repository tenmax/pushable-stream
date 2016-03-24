package io.tenmax.pstream.impl;

import java.util.HashMap;
import java.util.stream.Collector;

public class CollectedStream<I, O, R>
        extends BasePushableStream<I, O> {

    MyCollecotr<O, ?, R> myCollector;

    public <A> CollectedStream(
            BasePushableStream<I, O> upStream,
            Collector<O, A, R> collector) {
        super(upStream.sourceStream);

        myCollector = new MyCollecotr<>(collector);

        upStream.downStream = (O output) -> {
            myCollector.push(output);
            pushDownStream(output);
        };
    }


    R snapshot() {
        return myCollector.snapshot();
    }


    class MyCollecotr<T, A, R> {
        Collector<T, A, R> collector;
        HashMap<Thread, A> accByThread = new HashMap<>();

        MyCollecotr(Collector<T, A, R> collector) {
            this.collector = collector;
        }

        public void push(T item) {
            collector.accumulator().accept(acc(), item);
        }

        A acc() {
            Thread t = Thread.currentThread();

            if (!accByThread.containsKey(t)) {
                synchronized (this) {
                    if (!accByThread.containsKey(Thread.currentThread())) {
                        accByThread.put(t, collector.supplier().get());
                    }
                }
            }

            return accByThread.get(t);
        }

        public R snapshot() {
            synchronized (this) {
                A acc = collector.supplier().get();

                for (A acc2 : accByThread.values()) {
                    acc = collector.combiner().apply(acc, acc2);
                }

                return collector.finisher().apply(acc);
            }
        }
    }

}
