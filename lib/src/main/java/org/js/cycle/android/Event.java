package org.js.cycle.android;

import android.view.View;

public final class Event {
  private final String name;
  private final View view;

  Event(String name, View view) {
    this.name = name;
    this.view = view;
  }

  public <T extends View> T view() {
    //noinspection unchecked
    return (T) view;
  }

  public String name() {
    return name;
  }
}
