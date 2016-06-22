package org.js.cycle.android;

import rx.Observable;

public class PropsSource implements Source {
  public static final String NAME = "PROPS";
  private Observable<? extends ComponentProperties> propsObservable;

  public PropsSource(Observable<? extends ComponentProperties> propsObservable) {
    this.propsObservable = propsObservable;
  }

  public Observable<? extends ComponentProperties> observable() {
    return propsObservable;
  }

  @Override public String name() {
    return NAME;
  }

  @Override public void apply(Observable<?> stream) {
    //noinspection unchecked
    propsObservable = (Observable<? extends ComponentProperties>) stream;
  }
}
