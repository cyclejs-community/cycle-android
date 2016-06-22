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

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Event event = (Event) o;

    if (!name.equals(event.name)) return false;
    if (!view.equals(event.view)) return false;

    return true;
  }

  @Override public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + view.hashCode();
    return result;
  }
}
