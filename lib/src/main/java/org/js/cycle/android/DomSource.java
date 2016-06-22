package org.js.cycle.android;

import android.support.annotation.IdRes;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/** TODO */
public final class DomSource implements Source {
  private final DomDriver domDriver;
  private final List<DomSelection> selections = new ArrayList<>();

  public DomSource(DomDriver domDriver) {
    this.domDriver = domDriver;
    domDriver.events().subscribe(this::onDomEvent);
  }

  public DomSelection select(@IdRes int viewId) {
    DomSelection selection = new DomSelection(viewId);
    selections.add(selection);
    return selection;
  }

  @Override public String name() {
    return "DOM";
  }

  private void onDomEvent(Event event) {
    for (DomSelection selection : selections) {
      if (Util.viewMatchesSelector(event.view(), selection.idSelectors())) {
        selection.emit(event);
      }
    }
  }

  @Override public void apply(Observable<?> stream) {
    domDriver.apply(stream);
  }
}
