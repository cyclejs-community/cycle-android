package org.js.cycle.android;

import retrofit2.Response;
import rx.Observable;

public final class HttpSink implements Sink<Observable<? extends Response<?>>> {
  private final Observable<Observable<? extends Response<?>>> requestStream;

  private HttpSink(Observable<Observable<? extends Response<?>>> requestStream) {
    this.requestStream = requestStream;
  }

  @Override public String name() {
    return "HTTP";
  }

  @Override public Observable<Observable<? extends Response<?>>> stream() {
    return requestStream;
  }

  public static HttpSink create(Observable<Observable<? extends Response<?>>> requestStream) {
    return new HttpSink(requestStream);
  }
}
