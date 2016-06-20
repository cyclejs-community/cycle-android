package org.js.cycle.android;

import java.util.ArrayList;
import java.util.Arrays;

import rx.Observable;

@SuppressWarnings("rawtypes")
public final class Sinks extends ArrayList<Sink> {
  private Sinks(Sink... sinks) {
    super(Arrays.asList(sinks));
  }

  public static Sinks create(Sink... sinks) {
    return new Sinks(sinks);
  }

  public <T> Observable<T> findStreamOrThrow(String name) {
    for (Sink sink : this) {
      if (sink.name().equals(name)) {
        //noinspection unchecked
        return (Observable<T>) sink.stream();
      }
    }
    throw new IllegalArgumentException("Cannot find Sink with name " + name);
  }
}
