package org.js.cycle.android;

import retrofit2.Response;
import rx.Observable;

public final class HttpSource implements Source {
  private final HttpDriver httpDriver;

  public HttpSource(HttpDriver httpDriver) {
    this.httpDriver = httpDriver;
  }

  public <T> Observable<Observable<Response<T>>> observable() {
    //noinspection unchecked
    return (Observable<Observable<Response<T>>>) httpDriver.sink();
  }

  @Override public String name() {
    return "HTTP";
  }

  @Override public void apply(Observable<?> source) {
    httpDriver.apply(source);
  }
}
