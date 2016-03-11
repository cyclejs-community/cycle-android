package org.js.cycle.android;

import rx.Observable;

public final class Selector {
  private final Observable<Event> events;

  Selector(Observable<Event> events) {
    this.events = events;
  }

  public Observable<Event> events(String eventName) {
    return events.filter(e -> e.name().equals(eventName));
  }
}
