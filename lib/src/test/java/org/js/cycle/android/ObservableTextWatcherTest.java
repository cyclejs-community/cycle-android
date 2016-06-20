package org.js.cycle.android;

import android.widget.EditText;

import org.junit.Test;

import rx.observers.TestSubscriber;

import static org.mockito.Mockito.mock;

public class ObservableTextWatcherTest {
  @Test public void testObservable() {
    EditText editText = mock(EditText.class);
    ObservableTextWatcher textWatcher = ObservableTextWatcher.create(editText);
    TestSubscriber<Event> subscriber = new TestSubscriber<>();
    textWatcher.inputEventObservable().subscribe(subscriber);
    textWatcher.onTextChanged("foo", 0, 0, 0);
    subscriber.assertValue(new Event("input", editText));
    subscriber.assertNotCompleted();
    subscriber.assertNoErrors();
  }
}