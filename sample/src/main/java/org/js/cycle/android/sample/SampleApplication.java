package org.js.cycle.android.sample;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class SampleApplication extends Application {
  private final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
  private GithubService githubService;

  @Override public void onCreate() {
    super.onCreate();
    logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(logging)
        .build();
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .client(client)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build();
    githubService = retrofit.create(GithubService.class);
  }

  public GithubService githubService() {
    return githubService;
  }
}
