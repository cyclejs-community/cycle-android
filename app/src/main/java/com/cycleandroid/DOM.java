package com.cycleandroid;

import android.support.annotation.IdRes;

final class DOM {
  private final DOMDriver domDriver;

  DOM(DOMDriver domDriver) {
    this.domDriver = domDriver;
  }

  public Selector select(@IdRes int viewId) {
    return new Selector(domDriver.clickEvents()
        .filter(v -> v.getId() == viewId));
  }
}
