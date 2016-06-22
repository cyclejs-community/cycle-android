package org.js.cycle.android.sample;

import android.content.Context;
import android.content.Intent;

import org.js.cycle.android.DomSink;
import org.js.cycle.android.Sinks;
import org.js.cycle.android.Sources;

import rx.Observable;
import trikita.anvil.Anvil;

import static trikita.anvil.BaseDSL.withId;
import static trikita.anvil.BaseDSL.xml;
import static trikita.anvil.DSL.text;

public class CounterActivity extends SampleActivity {
  @Override protected Sinks main(Sources sources) {
    Observable<Integer> action$ = Observable.merge(
        sources.dom().select(R.id.btnIncrement).events("click").map(ev -> 1),
        sources.dom().select(R.id.btnDecrement).events("click").map(ev -> -1));

    DomSink domSink = DomSink.create(action$
        .startWith(0)
        .scan((x, y) -> x + y)
        .map(String::valueOf)
        .map(c -> (Anvil.Renderable) () ->
            xml(R.layout.vtree_counter, () ->
                withId(R.id.txtCount, () ->
                    text(c)))));

    return Sinks.create(domSink);
  }

  @Override protected int menuItemId() {
    return R.id.nav_counter;
  }

  static Intent newIntent(Context context) {
    return new Intent(context, CounterActivity.class);
  }
}
