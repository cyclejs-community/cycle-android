package org.js.cycle.android;

import rx.Observable;

public interface Sink<T> {
  String name();
  Observable<T> stream();

  class Factory {
    public static <T> Sink<T> create(String name, Observable<T> stream) {
      return new Sink<T>() {
        @Override public String name() {
          return name;
        }

        @Override public Observable<T> stream() {
          return stream;
        }
      };
    }
  }
}
