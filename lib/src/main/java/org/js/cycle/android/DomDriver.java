package org.js.cycle.android;

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
  private TouchEventInterceptingLayout touchInterceptor;

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
        maybeInjectTouchInterceptor();
        setTextChangeListeners();
      }
    });
  }

  private void setTextChangeListeners() {
    for (View focusable : touchInterceptor.getFocusables(View.FOCUS_FORWARD)) {
      if (focusable instanceof EditText) {
        EditText editText = (EditText) focusable;
        if (editText.getTag() == null || !(editText.getTag() instanceof ObservableTextWatcher)) {
          ObservableTextWatcher watcher = ObservableTextWatcher.create(editText);
          watcher.inputEventObservable().subscribe(domEvents::onNext);
          editText.addTextChangedListener(watcher);
          editText.setTag(watcher);
        }
      } else if (focusable instanceof NumberPicker) {
        NumberPicker numberPicker = (NumberPicker) focusable;
        if (numberPicker.getTag() == null ||
            !(numberPicker.getTag() instanceof ObservableOnValueChangeListener)) {
          ObservableOnValueChangeListener listener = new ObservableOnValueChangeListener();
          listener.observable().subscribe(domEvents::onNext);
          numberPicker.setOnValueChangedListener(listener);
          numberPicker.setTag(listener);
        }
      }
    }
  }

  private void maybeInjectTouchInterceptor() {
    if (touchInterceptor == null) {
      if (root.getParent() == null || !(root.getParent() instanceof ViewGroup)) {
        throw new IllegalStateException("The root View must have a parent ViewGroup.");
      }
      if (root.getParent() instanceof TouchEventInterceptingLayout) {
        touchInterceptor = (TouchEventInterceptingLayout) root.getParent();
      } else {
        touchInterceptor = new TouchEventInterceptingLayout(root.getContext());
        touchInterceptor.clickEventsObservable().subscribe(domEvents::onNext);
        Util.wrapViewWith(root, touchInterceptor);
      }
    }
  }

  Observable<Event> events() {
    return domEvents;
  }
}
