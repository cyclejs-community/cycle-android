package org.js.cycle.android;

import rx.Observable;

public interface Driver {
  void apply(Observable<?> stream);
}
