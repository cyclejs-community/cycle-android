package org.js.cycle.android.sample;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GithubService {
  @GET("search/repositories") Observable<Response<SearchResponse>> search(@Query("q") String query);
}
