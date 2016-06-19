package org.js.cycle.android;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("rawtypes")
public final class Sinks extends ArrayList<Sink> {

  private Sinks(Sink... sinks) {
    super(Arrays.asList(sinks));
  }

  public static Sinks create(Sink... sinks) {
    return new Sinks(sinks);
  }

  public Sink findSinkByName(String name) {
    for (Sink sink : this) {
      if (sink.name().equals(name)) {
        return sink;
      }
    }
    return null;
  }
}
