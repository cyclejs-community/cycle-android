package org.js.cycle.android;

import rx.Observable;
import trikita.anvil.Anvil;

public final class DomSink implements Sink<Anvil.Renderable> {
  private final Observable<Anvil.Renderable> vtree;

  private DomSink(Observable<Anvil.Renderable> vtree) {
    this.vtree = vtree;
  }

  public static DomSink create(Observable<Anvil.Renderable> vtree) {
    return new DomSink(vtree);
  }

  @Override public String name() {
    return "DOM";
  }

  @Override public Observable<Anvil.Renderable> stream() {
    return vtree;
  }
}
