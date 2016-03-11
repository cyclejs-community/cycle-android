package org.js.cycle.android;

import retrofit2.Response;
import rx.Observable;

public final class HttpSink implements Sink {
  private final Observable<Observable<Response<?>>> requestStream;

  private HttpSink(Observable<Observable<Response<?>>> requestStream) {
    this.requestStream = requestStream;
  }

  @Override public String name() {
    return "HTTP";
  }

  @Override public Observable<?> stream() {
    return requestStream;
  }

  public static <T> HttpSink create(Observable<T> requestStream) {
    //noinspection unchecked
    return new HttpSink((Observable<Observable<Response<?>>>) requestStream);
  }
}
