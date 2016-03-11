package org.js.cycle.android;

import rx.Observable;
import rx.functions.Action0;

public final class Sinks {
  private final Observable<Action0> dom;

  private Sinks(Observable<Action0> dom) {
    this.dom = dom;
  }

  public static Sinks create(Observable<Action0> dom) {
    return new Sinks(dom);
  }

  Observable<Action0> dom() {
    return dom;
  }
}
