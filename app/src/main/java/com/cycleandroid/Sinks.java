package com.cycleandroid;

import android.view.View;

import rx.Observable;

public final class Sinks {
  private final Observable<View> views;

  private Sinks(Observable<View> views) {
    this.views = views;
  }

  public static Sinks create(Observable<View> views) {
    return new Sinks(views);
  }

  public Observable<View> views() {
    return views;
  }
}
