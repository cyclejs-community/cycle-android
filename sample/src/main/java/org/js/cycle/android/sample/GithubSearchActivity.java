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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import static trikita.anvil.BaseDSL.MATCH;
import static trikita.anvil.BaseDSL.WRAP;
import static trikita.anvil.BaseDSL.size;
import static trikita.anvil.BaseDSL.withId;
import static trikita.anvil.BaseDSL.xml;
import static trikita.anvil.DSL.text;
import static trikita.anvil.DSL.textView;

public class GithubSearchActivity extends SampleActivity {
  @Override protected Sinks main(Sources sources) {
    SampleApplication application = (SampleApplication) getApplication();
    DomSink domSink = DomSink.create(sources.http()
        .<SearchResponse>observable()
        .flatMap(r -> r)
        .map(Response::body)
        .startWith(new SearchResponse())
        .observeOn(AndroidSchedulers.mainThread())
        .map(results -> (Action0) () -> xml(R.layout.vtree_search_github, () ->
            withId(R.id.layout_results, () -> {
              for (SearchResponse.SearchResponseItem item : results.items) {
                textView(() -> {
                  size(MATCH, WRAP);
                  text(item.full_name);
                });
              }
            }))));
    GithubService githubService = application.githubService();
    HttpSink httpSink = HttpSink.create(sources.dom()
        .select(R.id.editQuery)
        .events("input")
        .debounce(500, TimeUnit.MILLISECONDS)
        .map(ev -> ev.<EditText>view().getText().toString())
        .filter(q -> q.length() > 0)
        .map(q -> githubService.search(q).subscribeOn(Schedulers.io())));

    return Sinks.create(domSink, httpSink);
  }

  @Override protected int menuItemId() {
    return R.id.nav_search;
  }

  public static Intent newIntent(Context context) {
    return new Intent(context, GithubSearchActivity.class);
  }
}
