package com.cycleandroid;

import android.view.View;
import android.view.ViewGroup;

import rx.Observable;

final class ViewDriver {
  public static Observable<View> makeViewDriver(
      ViewGroup target, Observable<View> vtreeObservable) {
    vtreeObservable.subscribe(view -> renderVTree(target, view));
    return vtreeObservable;
  }

  private static void renderVTree(ViewGroup target, View vtree) {
    target.removeAllViews();
    target.addView(vtree);
  }
}
