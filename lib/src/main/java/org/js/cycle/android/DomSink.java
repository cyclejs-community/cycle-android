package org.js.cycle.android;

import rx.Observable;
import rx.functions.Action0;

public final class DomSink implements Sink {
  private final Observable<Action0> vtree;

  private DomSink(Observable<Action0> vtree) {
    this.vtree = vtree;
  }

  public static DomSink create(Observable<Action0> vtree) {
    return new DomSink(vtree);
  }

  @Override public String name() {
    return "DOM";
  }

  @Override public Observable<?> stream() {
    return vtree;
  }
}
