package org.js.cycle.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import rx.Observable;

import static org.js.cycle.android.Util.getClickTarget;

@SuppressLint("ViewConstructor")
class ClickEventInterceptingLayout extends FrameLayout {
  private final GestureDetector gestureDetector;
  private final Observable<Event> click$;

  ClickEventInterceptingLayout(Context context) {
    super(context);
    ObservableSingleTapGestureDetector singleTapDetector = new ObservableSingleTapGestureDetector();
    gestureDetector = new GestureDetector(getContext(), singleTapDetector);
    click$ = singleTapDetector.clickStream()
        .map(e -> getClickTarget(this, e))
        .map(v -> new Event("click", v));
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    gestureDetector.onTouchEvent(ev);
    return super.onInterceptTouchEvent(ev);
  }

  Observable<Event> clickEventsObservable() {
    return click$;
  }
}
