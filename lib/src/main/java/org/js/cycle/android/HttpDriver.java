package org.js.cycle.android;

import rx.Observable;

@SuppressWarnings("rawtypes")
public final class HttpDriver implements Driver {
  private Observable<?> sink;

  private HttpDriver() {
  }

  public static HttpDriver makeHttpDriver() {
    return new HttpDriver();
  }

  @Override public void apply(Observable<?> sink) {
    this.sink = sink;
  }

  public Observable<?> sink() {
    return sink;
  }
}
