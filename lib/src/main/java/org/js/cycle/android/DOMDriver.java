package org.js.cycle.android;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import rx.Observable;
import rx.functions.Action0;
import rx.subjects.PublishSubject;
import trikita.anvil.Anvil;

final class DOMDriver {
  private final ViewGroup root;
  private final PublishSubject<Event> events = PublishSubject.create();
  private TouchEventInterceptingLayout touchInterceptor;

  private DOMDriver(ViewGroup root, Observable<Action0> domSink) {
    this.root = root;
    domSink.subscribe(this::renderVTree);
  }

  public static DOMDriver makeDOMDriver(ViewGroup target, Observable<Action0> domSink) {
    return new DOMDriver(target, domSink);
  }

  private void renderVTree(Action0 action) {
    if (touchInterceptor == null) {
      touchInterceptor = new TouchEventInterceptingLayout(root.getContext());
      root.addView(touchInterceptor);
    }
    Anvil.mount(touchInterceptor, action::call);
  }

  Observable<Event> events() {
    return events;
  }

  class TouchEventInterceptingLayout extends FrameLayout {
    private GestureDetector gestureDetector;
    private final GestureDetector.SimpleOnGestureListener clickListener =
        new GestureDetector.SimpleOnGestureListener() {
          @Override public boolean onSingleTapUp(MotionEvent e) {
            events.onNext(new Event("click", getClickTarget(TouchEventInterceptingLayout.this, e)));
            return super.onSingleTapUp(e);
          }
        };

    public TouchEventInterceptingLayout(Context context) {
      super(context);
      init();
    }

    public TouchEventInterceptingLayout(Context context, AttributeSet attrs) {
      super(context, attrs);
      init();
    }

    public TouchEventInterceptingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      init();
    }

    private void init() {
      gestureDetector = new GestureDetector(getContext(), clickListener);
    }

    @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
      gestureDetector.onTouchEvent(ev);
      return super.onInterceptTouchEvent(ev);
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
      return super.onKeyDown(keyCode, event);
    }

    @Override public boolean dispatchKeyEvent(KeyEvent event) {
      if (event.getAction() == KeyEvent.ACTION_UP) {
        events.onNext(new Event("input", tryGetFocusedChild(this)));
      }
      return super.dispatchKeyEvent(event);
    }
  }

  @Nullable private static View tryGetFocusedChild(View view) {
    if (!(view instanceof ViewGroup) && view.isFocused()) {
      return view;
    }
    if (view instanceof ViewGroup) {
      return tryGetFocusedChild(((ViewGroup) view).getFocusedChild());
    }
    return null;
  }

  @Nullable private static View getClickTarget(View view, MotionEvent e) {
    boolean isHit = hitTest(e.getRawX(), e.getRawY(), view);
    if (isHit && !(view instanceof ViewGroup)) {
      return view;
    }
    if (view instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup) view;
      for (int i = 0; i < viewGroup.getChildCount(); i++) {
        View child = viewGroup.getChildAt(i);
        View clickTarget = getClickTarget(child, e);
        if (clickTarget != null) {
          return clickTarget;
        }
      }
    }
    return isHit ? view : null;
  }

  /**
   * Determines if given points are inside view
   *
   * @param x    - x coordinate of point
   * @param y    - y coordinate of point
   * @param view - view object to compare
   * @return true if the points are within view bounds, false otherwise
   */
  private static boolean hitTest(float x, float y, View view) {
    int location[] = new int[2];
    view.getLocationOnScreen(location);
    int viewX = location[0];
    int viewY = location[1];

    //point is inside view bounds
    if ((x > viewX && x < (viewX + view.getWidth())) &&
        (y > viewY && y < (viewY + view.getHeight()))) {
      return true;
    } else {
      return false;
    }
  }
}
