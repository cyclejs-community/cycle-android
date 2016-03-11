package org.js.cycle.android;

import android.support.annotation.IdRes;

public final class DOM {
  private final DOMDriver domDriver;

  DOM(DOMDriver domDriver) {
    this.domDriver = domDriver;
  }

  public Selector select(@IdRes int viewId) {
    return new Selector(domDriver.events()
        .filter(e -> e.view().getId() == viewId));
  }
}
