package org.js.cycle.android;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import rx.Observable;
import rx.Observer;
import rx.subjects.PublishSubject;
import trikita.anvil.Anvil;

/** TODO */
public final class DomDriver implements Driver {
  private final ViewGroup root;
  private final PublishSubject<Event> domEvents = PublishSubject.create();
  private ClickEventInterceptingLayout touchInterceptor;

  private DomDriver(ViewGroup root) {
    this.root = root;
  }

  public static DomDriver makeDomDriver(ViewGroup target) {
    return new DomDriver(target);
  }

  @Override public void apply(Observable<?> stream) {
    //noinspection unchecked
    ((Observable<Anvil.Renderable>) stream).subscribe(new Observer<Anvil.Renderable>() {
      @Override public void onCompleted() {
      }

      @Override public void onError(Throwable e) {
        throw new RuntimeException(e);
      }

      @Override public void onNext(Anvil.Renderable renderable) {
        Anvil.mount(root, renderable);
        maybeInjectClickInterceptingLayout();
        recursivelySetListeners(touchInterceptor);
      }
    });
  }

  private void maybeInjectClickInterceptingLayout() {
    if (touchInterceptor == null) {
      if (root.getParent() == null || !(root.getParent() instanceof ViewGroup)) {
        throw new IllegalStateException("The root View must have a parent ViewGroup.");
      }
      if (root.getParent() instanceof ClickEventInterceptingLayout) {
        touchInterceptor = (ClickEventInterceptingLayout) root.getParent();
      } else {
        touchInterceptor = new ClickEventInterceptingLayout(root.getContext());
        touchInterceptor.clickEventsObservable().subscribe(domEvents::onNext);
        Util.wrapViewWith(root, touchInterceptor);
      }
    }
  }

  private void recursivelySetListeners(ViewGroup viewGroup) {
    int count = viewGroup.getChildCount();
    for (int i = 0; i < count; i++) {
      View view = viewGroup.getChildAt(i);
      if (view instanceof NumberPicker) {
        subscribe(maybeSetValueChangedListener((NumberPicker) view));
      } else if (view instanceof EditText) {
        subscribe(maybeSetTextChangedListener((EditText) view));
      } else if (view instanceof ViewGroup) {
        recursivelySetListeners((ViewGroup) view);
      }
    }
  }

  private void subscribe(@Nullable Observable<Event> observable) {
    if (observable != null) {
      observable.subscribe(domEvents::onNext);
    }
  }

  @Nullable private Observable<Event> maybeSetValueChangedListener(NumberPicker view) {
    if (view.getTag() == null || !(view.getTag() instanceof NumberPicker.OnValueChangeListener)) {
      ObservableOnValueChangeListener listener = new ObservableOnValueChangeListener();
      view.setOnValueChangedListener(listener);
      view.setTag(listener);
      return listener.observable();
    } else {
      return null;
    }
  }

  @Nullable private Observable<Event> maybeSetTextChangedListener(EditText editText) {
    if (editText.getTag() == null || !(editText.getTag() instanceof ObservableTextWatcher)) {
      ObservableTextWatcher watcher = ObservableTextWatcher.create(editText);
      editText.addTextChangedListener(watcher);
      editText.setTag(watcher);
      return watcher.observable();
    } else {
      return null;
    }
  }

  Observable<Event> events() {
    return domEvents;
  }
}
