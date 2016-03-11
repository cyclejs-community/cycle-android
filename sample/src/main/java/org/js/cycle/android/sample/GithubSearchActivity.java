package org.js.cycle.android.sample;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import org.js.cycle.android.DomSink;
import org.js.cycle.android.HttpSink;
import org.js.cycle.android.Sinks;
import org.js.cycle.android.Sources;

import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import rx.functions.Action0;

import static trikita.anvil.BaseDSL.withId;
import static trikita.anvil.BaseDSL.xml;
import static trikita.anvil.DSL.text;

public class GithubSearchActivity extends SampleActivity {
  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("https://api.github.com")
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create())
      .build();
  private final GithubService githubService = retrofit.create(GithubService.class);;

  @Override protected Sinks main(Sources sources) {
    DomSink domSink = DomSink.create(sources.http()
        .<SearchResponse>observable()
        .flatMap(r -> r)
        .map(Response::body)
        .startWith(new SearchResponse())
        .map(results -> (Action0) () -> xml(R.layout.vtree_search_github, () ->
            withId(R.id.layout_results, () -> {
              for (SearchResponse.SearchResponseItem item : results.items) {
                text(item.full_name);
              }
            }))));

    HttpSink httpSink = HttpSink.create(sources.dom()
        .select(R.id.editQuery)
        .events("input")
        .debounce(500, TimeUnit.MILLISECONDS)
        .map(ev -> ev.<EditText>view().getText().toString())
        .filter(q -> q.length() > 0)
        .map(githubService::search));

    return Sinks.create(domSink, httpSink);
  }

  @Override protected int menuItemId() {
    return R.id.nav_search;
  }

  public static Intent newIntent(Context context) {
    return new Intent(context, GithubSearchActivity.class);
  }
}
