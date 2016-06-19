package org.js.cycle.android;

import rx.Observable;

public class PropsSource implements Source {
    private Observable<? extends Props> propsObservable;

    public PropsSource(Observable<? extends Props> propsObservable) {
        this.propsObservable = propsObservable;
    }

    public Observable<? extends Props> observable() {
        return propsObservable;
    }

    @Override public String name() {
        return "PROPS";
    }

    @Override public void apply(Observable<?> stream) {
        //noinspection unchecked
        propsObservable = (Observable<? extends Props>) stream;
    }

}
