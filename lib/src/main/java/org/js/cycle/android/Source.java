package org.js.cycle.android;

import rx.Observable;

public interface Source {
  String name();
  void apply(Observable<?> stream);
}
