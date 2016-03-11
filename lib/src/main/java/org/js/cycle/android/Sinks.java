package org.js.cycle.android;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("rawtypes")
public final class Sinks {
  private final List<Sink> sinks;

  private Sinks(Sink... sinks) {
    this.sinks = Arrays.asList(sinks);
  }

  public static Sinks create(Sink... sinks) {
    return new Sinks(sinks);
  }

  public DomSink dom() {
    return (DomSink) findSinkByName("DOM");
  }

  public HttpSink http() {
    return (HttpSink) findSinkByName("HTTP");
  }

  public List<Sink> list() {
    return sinks;
  }

  private Sink findSinkByName(String name) {
    for (Sink sink : sinks) {
      if (sink.name().equals(name)) {
        return sink;
      }
    }
    return null;
  }
}
