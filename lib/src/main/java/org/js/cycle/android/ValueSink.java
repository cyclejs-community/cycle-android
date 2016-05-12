package org.js.cycle.android;

import rx.Observable;

/**
 * Created by chris on 05/05/16.
 */
public class ValueSink implements Sink {
    private Observable<Integer> valueStream;

    public ValueSink(Observable<Integer> valueStream) {
        this.valueStream = valueStream;
    }

    @Override
    public String name() {
        return "VALUE";
    }


    @Override public Observable<Integer> stream() {
        return valueStream;
    }

    public static <T> ValueSink create(Observable<T> valueStream) {
        //noinspection unchecked
        return new ValueSink((Observable<Integer>) valueStream);
    }
}
