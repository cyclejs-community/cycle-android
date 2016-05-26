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
}
