package org.js.cycle.android;

import android.view.GestureDetector;
import android.view.MotionEvent;

import rx.Observable;
import rx.subjects.PublishSubject;

final class ObservableSingleTapGestureDetector extends GestureDetector.SimpleOnGestureListener {
  private final PublishSubject<MotionEvent> subject = PublishSubject.create();

  @Override public boolean onSingleTapUp(MotionEvent e) {
    subject.onNext(e);
    return super.onSingleTapUp(e);
  }

  Observable<MotionEvent> clickStream() {
    return subject;
  }
}
