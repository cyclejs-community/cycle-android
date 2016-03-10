package com.cycleandroid;

import android.view.View;
import android.view.ViewGroup;

import rx.functions.Func1;
import rx.subjects.ReplaySubject;

final class Cycle {
  public static void run(Func1<Sources, Sinks> main, ViewGroup target) {
    ReplaySubject<View> sinkProxy = ReplaySubject.create(1);
    Sources sources = Sources.create(ViewDriver.makeViewDriver(target, sinkProxy));
    Sinks sinks = main.call(sources);
    sinks.views().subscribe(sinkProxy);
  }
}
