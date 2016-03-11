package org.js.cycle.android;

import android.view.ViewGroup;

import rx.functions.Action0;
import rx.functions.Func1;
import rx.subjects.ReplaySubject;

public final class Cycle {
  public static void run(Func1<Sources, Sinks> main, ViewGroup target) {
    ReplaySubject<Action0> sinkProxy = ReplaySubject.create(1);
    Sources sources = Sources.create(new DOM(DOMDriver.makeDOMDriver(target, sinkProxy)));
    Sinks sinks = main.call(sources);
    sinks.dom().subscribe(sinkProxy);
  }
}
