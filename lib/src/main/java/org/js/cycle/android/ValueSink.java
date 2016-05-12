package org.js.cycle.android;

import rx.Observable;

public class ValueSink implements Sink {
    private Observable<?> valueStream;

    public ValueSink(Observable<?> valueStream) {
        this.valueStream = valueStream;
    }

    @Override
    public String name() {
        return "VALUE";
    }


    @Override public Observable<?> stream() {
        return valueStream;
    }

    public static <T> ValueSink create(Observable<T> valueStream) {
        return new ValueSink(valueStream);
    }
}
