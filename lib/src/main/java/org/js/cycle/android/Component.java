package org.js.cycle.android;

public interface Component {
  Sinks create(Sources sources);
}
