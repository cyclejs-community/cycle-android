package org.js.cycle.android;

import rx.Observable;

public interface Sink {
  String name();
  Observable<?> stream();

  class Factory {
    public static Sink create(String name, Observable<?> stream) {
      return new Sink() {
        @Override public String name() {
          return name;
        }

        @Override public Observable<?> stream() {
          return stream;
        }
      };
    }
  }
}
