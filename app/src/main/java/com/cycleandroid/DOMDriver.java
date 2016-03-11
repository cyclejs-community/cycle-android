package com.cycleandroid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import rx.Observable;
import rx.subjects.PublishSubject;

final class DOMDriver {
  private final ViewGroup root;
  private final PublishSubject<View> clickEvents = PublishSubject.create();
  private TouchEventInterceptingLayout touchInterceptor;

  private DOMDriver(ViewGroup root, Observable<View> vtreeObservable) {
    this.root = root;
    vtreeObservable.subscribe(this::renderVTree);
  }

  public static DOMDriver makeDOMDriver(ViewGroup target, Observable<View> vtreeObservable) {
    return new DOMDriver(target, vtreeObservable);
  }

  private void renderVTree(View vtree) {
    root.removeAllViews();
    if (touchInterceptor == null) {
      touchInterceptor = new TouchEventInterceptingLayout(root.getContext());
    }
    touchInterceptor.removeAllViews();
    root.addView(touchInterceptor);
    touchInterceptor.addView(vtree);
  }

  Observable<View> clickEvents() {
    return clickEvents;
  }

  class TouchEventInterceptingLayout extends FrameLayout {
    private GestureDetector gestureDetector;
    private final GestureDetector.SimpleOnGestureListener clickListener =
        new GestureDetector.SimpleOnGestureListener() {
          @Override public boolean onSingleTapUp(MotionEvent e) {
            clickEvents.onNext(getClickTarget(TouchEventInterceptingLayout.this, e));
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
  }

  private static View getClickTarget(View view, MotionEvent e) {
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
