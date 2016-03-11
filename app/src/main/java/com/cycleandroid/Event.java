package com.cycleandroid;

import android.view.View;

final class Event {
  private final String name;
  private final View view;

  Event(String name, View view) {
    this.name = name;
    this.view = view;
  }
}
