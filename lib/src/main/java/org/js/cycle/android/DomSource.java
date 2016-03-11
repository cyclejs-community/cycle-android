package org.js.cycle.android;

import android.support.annotation.IdRes;

import rx.Observable;

public final class DomSource implements Source {
  private final DomDriver domDriver;

  public DomSource(DomDriver domDriver) {
    this.domDriver = domDriver;
  }

  public Selector select(@IdRes int viewId) {
    return new Selector(domDriver.events()
        .filter(e -> e.view().getId() == viewId));
  }

  @Override public String name() {
    return "DOM";
  }

  @Override public void apply(Observable<?> stream) {
    domDriver.apply(stream);
  }
}
