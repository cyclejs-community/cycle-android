package org.js.cycle.android;

import android.view.ViewGroup;

import org.junit.Test;

import rx.Observable;
import rx.functions.Func1;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.mock;

public class CycleTest {
  private static class FakeSource implements Source {
    private final String name;
    Observable<Integer> sink;

    FakeSource(String name) {
      this.name = name;
    }

    @Override public String name() {
      return name;
    }

    @Override public void apply(Observable<?> stream) {
      //noinspection unchecked
      this.sink = (Observable<Integer>) stream;
    }
  }

  private final ViewGroup app = mock(ViewGroup.class);
  private final DomSource domSource = new DomSource(DomDriver.makeDomDriver(app));

  @Test public void connectSourcesAndSinks() {
    Func1<Sources, Sinks> main = (sources) -> Sinks.create(
        Sink.Factory.create("test", Observable.just(1)));
    FakeSource fakeSource = new FakeSource("test");

    Cycle.run(main, fakeSource, domSource);

    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    fakeSource.sink.subscribe(subscriber);
    subscriber.assertCompleted();
    subscriber.assertNoErrors();
    subscriber.assertValue(1);
  }

  @Test public void doNotConnectIfNameDoesNotMatch() {
    Func1<Sources, Sinks> main = (sources) -> Sinks.create(
        Sink.Factory.create("foo", Observable.just(1)));
    FakeSource fakeSource = new FakeSource("test");

    Cycle.run(main, fakeSource, domSource);

    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    fakeSource.sink.subscribe(subscriber);
    subscriber.assertNotCompleted();
    subscriber.assertNoErrors();
    subscriber.assertNoValues();
  }

  @Test public void isolateSinks() {
    Func1<Sources, Sinks> main = (sources) -> Sinks.create(
        Sink.Factory.create("test", Observable.just(1)),
        Sink.Factory.create("foo", Observable.just(2)));
    FakeSource testSource = new FakeSource("test");
    FakeSource fooSource = new FakeSource("foo");

    Cycle.run(main, testSource, fooSource, domSource);

    TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
    testSource.sink.subscribe(testSubscriber);
    testSubscriber.assertCompleted();
    testSubscriber.assertNoErrors();
    testSubscriber.assertValue(1);

    TestSubscriber<Integer> fooSubscriber = new TestSubscriber<>();
    fooSource.sink.subscribe(fooSubscriber);
    fooSubscriber.assertCompleted();
    fooSubscriber.assertNoErrors();
    fooSubscriber.assertValue(2);
  }
}