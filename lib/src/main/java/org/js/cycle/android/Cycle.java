package org.js.cycle.android;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.functions.Func1;
import rx.subjects.ReplaySubject;

public final class Cycle {
  public static void run(Func1<Sources, Sinks> main, Source... sourceList) {
    Sources sources = Sources.create(sourceList);
    Map<String, ReplaySubject<?>> sinkProxies = createAndApplySinkProxies(sources);
    Sinks sinks = main.call(sources);
    subscribeProxies(sinkProxies, sinks);
  }

  private static void subscribeProxies(Map<String, ReplaySubject<?>> sinkProxies, Sinks sinks) {
    for (Sink sink : sinks.list()) {
      //noinspection rawtypes
      Observer proxy = sinkProxies.get(sink.name());
      //noinspection unchecked
      sink.stream().subscribe(proxy);
    }
  }

  private static Map<String, ReplaySubject<?>> createAndApplySinkProxies(Sources sources) {
    Map<String, ReplaySubject<?>> sinkProxies = new HashMap<>();
    for (Source source : sources.list()) {
      ReplaySubject<Object> sinkProxy = ReplaySubject.create(1);
      sinkProxies.put(source.name(), sinkProxy);
      source.apply(sinkProxy);
    }
    return sinkProxies;
  }
}
