package com.cycleandroid;

import android.view.View;

import rx.Observable;

public class Sources {
  private final Observable<View> views;

  private Sources(Observable<View> views) {
    this.views = views;
  }

  public static Sources create(Observable<View> views) {
    return new Sources(views);
  }

  public Observable<View> views() {
    return views;
  }
}
