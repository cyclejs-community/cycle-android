package org.js.cycle.android;

import rx.Observable;

public interface Sink {
  String name();
  Observable<?> stream();
}
