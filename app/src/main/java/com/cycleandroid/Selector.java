package com.cycleandroid;

import android.view.View;

import rx.Observable;

final class Selector {
  private final Observable<View> clickEvents;

  Selector(Observable<View> clickEvents) {
    this.clickEvents = clickEvents;
  }

  public Observable<Event> events(String event) {
    if ("click".equals(event)) {
      return clickEvents.map(v -> new Event(event, v));
    }
    throw new RuntimeException("Event not implemented");
  }
}
