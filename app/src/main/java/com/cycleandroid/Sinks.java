package com.cycleandroid;

import android.view.View;

import rx.Observable;

public final class Sinks {
  private final Observable<View> dom;

  private Sinks(Observable<View> dom) {
    this.dom = dom;
  }

  public static Sinks create(Observable<View> dom) {
    return new Sinks(dom);
  }

  Observable<View> dom() {
    return dom;
  }
}
