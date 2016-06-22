package org.js.cycle.android;

import android.support.annotation.IdRes;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

public final class DomSelection {
  private final List<Integer> idSelectors = new ArrayList<>(1);
  private final PublishSubject<Event> event$ = PublishSubject.create();

  DomSelection(int viewId) {
    select(viewId);
  }

  public DomSelection select(@IdRes int viewId) {
    idSelectors.add(viewId);
    return this;
  }

  public Observable<Event> events(String eventName) {
    return event$.filter(e -> e.name().equals(eventName));
  }

  void emit(Event e) {
    event$.onNext(e);
  }

  List<Integer> idSelectors() {
    return idSelectors;
  }
}
