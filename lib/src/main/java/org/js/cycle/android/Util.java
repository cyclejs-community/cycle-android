package org.js.cycle.android;

import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

final class Util {
  private Util() {
  }

  /**
   * Returns whether the provided View matches the list of query selectors. Search is performed by
   * traversing the list of filters backwards and trying to match its ID to any of the View's parents
   * until there are no more filters or Views available.
   */
  static boolean viewMatchesSelector(View view, List<Integer> filteringScope) {
    if (filteringScope.isEmpty()) {
      return true;
    }
    if (filteringScope.size() == 1) {
      return view.getId() == filteringScope.get(0);
    }
    List<Integer> newScope = new ArrayList<>(filteringScope);
    newScope.remove(newScope.get(newScope.size() - 1));
    return view.getParent() != null && viewMatchesSelector((View) view.getParent(), newScope);
  }

  static void wrapViewWith(View view, ViewGroup wrapper) {
    ViewGroup parent = (ViewGroup) view.getParent();
    int rootIndex = parent.indexOfChild(view);
    parent.removeView(view);
    parent.addView(wrapper, rootIndex);
    wrapper.addView(view);
  }

  /**
   * Returns the view that originated the provided MotionEvent by searching recursively through the
   * provided View layout tree. Returns null if none found.
   */
  @Nullable static View getClickTarget(View view, MotionEvent e) {
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

    return (x > viewX && x < (viewX + view.getWidth())) &&
        (y > viewY && y < (viewY + view.getHeight()));
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * @param reference an object reference
   * @param errorMessage the exception message to use if the check fails; will be converted to a
   *     string using {@link String#valueOf(Object)}
   * @return the non-null reference that was validated
   * @throws NullPointerException if {@code reference} is null
   */
  static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
    if (reference == null) {
      throw new NullPointerException(String.valueOf(errorMessage));
    }
    return reference;
  }
}
