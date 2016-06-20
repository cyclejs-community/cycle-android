package org.js.cycle.android;

import android.widget.NumberPicker;

import rx.Observable;
import rx.subjects.PublishSubject;

final class ObservableOnValueChangeListener implements NumberPicker.OnValueChangeListener {
  private final PublishSubject<Event> observable = PublishSubject.create();

  @Override public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
    observable.onNext(new Event("change", picker));
  }

  public Observable<Event> observable() {
    return observable;
  }
}
