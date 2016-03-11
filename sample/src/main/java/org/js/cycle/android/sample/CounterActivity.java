package org.js.cycle.android.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.js.cycle.android.Sinks;
import org.js.cycle.android.Sources;

import rx.Observable;
import rx.functions.Action0;

import static trikita.anvil.BaseDSL.text;
import static trikita.anvil.BaseDSL.withId;
import static trikita.anvil.BaseDSL.xml;

public class CounterActivity extends SampleActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    navigationView.getMenu().findItem(R.id.nav_counter).setChecked(true);
  }

  @Override protected Sinks main(Sources sources) {
    Observable<Integer> actionStream = Observable.merge(
        sources.dom().select(R.id.btnIncrement).events("click").map(ev -> 1),
        sources.dom().select(R.id.btnDecrement).events("click").map(ev -> -1));

    return Sinks.create(actionStream
        .startWith(0)
        .scan((x, y) -> x + y)
        .map(String::valueOf)
        .doOnNext(s -> Log.d("main", s))
        .map(c -> (Action0) () ->
            xml(R.layout.vtree_counter, () ->
                withId(R.id.txtCount, () ->
                    text(new StringBuilder(c))))));
  }

  public static Intent newIntent(Context context) {
    return new Intent(context, CounterActivity.class);
  }
}
