package org.js.cycle.android;

import android.view.ViewGroup;

import org.junit.Test;

import java.util.Arrays;

import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class DomSourceTest {
  @Test public void select() {
    ViewGroup app = mock(ViewGroup.class);
    DomSource domSource = new DomSource(DomDriver.makeDomDriver(app));
    //noinspection ResourceType
    DomSelection selection = domSource.select(1).select(2);
    assertThat(selection.idSelectors()).isEqualTo(Arrays.asList(1, 2));
  }

  @Test public void emit() {
    ViewGroup app = mock(ViewGroup.class);
    DomSource domSource = new DomSource(DomDriver.makeDomDriver(app));
    //noinspection ResourceType
    DomSelection selection = domSource.select(1);
    TestSubscriber<Event> subscriber = new TestSubscriber<>();
    selection.events("foo").subscribe(subscriber);
    Event event = new Event("foo", app);
    selection.emit(event);
    subscriber.assertValue(event);
    subscriber.assertNoErrors();
    subscriber.assertNotCompleted();
  }

  @Test public void emitFilter() {
    ViewGroup app = mock(ViewGroup.class);
    DomSource domSource = new DomSource(DomDriver.makeDomDriver(app));
    //noinspection ResourceType
    DomSelection selection = domSource.select(1);
    TestSubscriber<Event> subscriber = new TestSubscriber<>();
    selection.events("bar").subscribe(subscriber);
    Event event = new Event("foo", app);
    selection.emit(event);
    subscriber.assertNoValues();
    subscriber.assertNoErrors();
    subscriber.assertNotCompleted();
  }
}