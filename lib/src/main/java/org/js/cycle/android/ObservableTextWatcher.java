package org.js.cycle.android;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import rx.Observable;
import rx.subjects.PublishSubject;

class ObservableTextWatcher implements TextWatcher {
  private final PublishSubject<EditText> textChanged$ = PublishSubject.create();
  private final EditText editText;

  private ObservableTextWatcher(EditText editText) {
    this.editText = editText;
  }

  static ObservableTextWatcher create(EditText editText) {
    return new ObservableTextWatcher(editText);
  }

  @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }

  @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
    textChanged$.onNext(editText);
  }

  @Override public void afterTextChanged(Editable s) {
  }

  Observable<Event> inputEventObservable() {
    return textChanged$.map(e -> new Event("input", editText));
  }
}
