package org.js.cycle.android;

import java.util.Arrays;
import java.util.List;

public class Sources {
  private final List<Source> sources;

  private Sources(Source... sources) {
    this.sources = Arrays.asList(sources);
  }

  public static Sources create(Source... sources) {
    return new Sources(sources);
  }

  public DomSource dom() {
    return (DomSource) findSourceByName("DOM");
  }

  public HttpSource http() {
    return (HttpSource) findSourceByName("HTTP");
  }

  public List<Source> list() {
    return sources;
  }

  private Source findSourceByName(String name) {
    for (Source source : sources) {
      if (source.name().equals(name)) {
        return source;
      }
    }
    throw new IllegalArgumentException("Source not found with name=" + name);
  }

  public PropsSource props() {
    return (PropsSource) findSourceByName("PROPS");
  }
}
