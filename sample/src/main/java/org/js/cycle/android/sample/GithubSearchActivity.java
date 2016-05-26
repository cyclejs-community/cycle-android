package org.js.cycle.android.sample;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.EditText;

import org.js.cycle.android.DomSink;
import org.js.cycle.android.HttpSink;
import org.js.cycle.android.Sinks;
import org.js.cycle.android.Sources;

import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import trikita.anvil.Anvil;

import static trikita.anvil.BaseDSL.withId;
import static trikita.anvil.BaseDSL.xml;
import static trikita.anvil.DSL.text;
import static trikita.anvil.DSL.textView;
import static trikita.anvil.recyclerview.Recycler.Adapter.simple;
import static trikita.anvil.recyclerview.Recycler.adapter;
import static trikita.anvil.recyclerview.Recycler.hasFixedSize;
import static trikita.anvil.recyclerview.Recycler.layoutManager;

public class GithubSearchActivity extends SampleActivity {
  @Override protected Sinks main(Sources sources) {
    SampleApplication application = (SampleApplication) getApplication();
    LinearLayoutManager layoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    DomSink domSink = DomSink.create(sources.http()
        .<SearchResponse>observable()
        .flatMap(r -> r)
        .map(Response::body)
        .startWith(new SearchResponse())
        .observeOn(AndroidSchedulers.mainThread())
        .map(results -> (Anvil.Renderable) () -> xml(R.layout.vtree_search_github, () ->
            withId(R.id.recycler, () -> {
              layoutManager(layoutManager);
              hasFixedSize(true);
              adapter(simple(results.items, (viewHolder) ->
                  textView(() ->
                      text(results.items.get(viewHolder.getAdapterPosition()).name))));
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
