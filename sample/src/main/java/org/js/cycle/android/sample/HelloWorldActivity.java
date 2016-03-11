package org.js.cycle.android.sample;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import org.js.cycle.android.DomSink;
import org.js.cycle.android.Sinks;
import org.js.cycle.android.Sources;

import rx.functions.Action0;

import static trikita.anvil.BaseDSL.withId;
import static trikita.anvil.BaseDSL.xml;
import static trikita.anvil.DSL.text;

public class HelloWorldActivity extends SampleActivity {
  @Override protected Sinks main(Sources sources) {
    DomSink domSink = DomSink.create(sources.dom()
        .select(R.id.editName)
        .events("input")
        .map(ev -> ev.<EditText>view().getText().toString())
        .startWith("")
        .map(name -> (Action0) () ->
            xml(R.layout.vtree_helloworld, () ->
                withId(R.id.txtHelloWorld, () ->
                    text("Hello, " + name)))));

    return Sinks.create(domSink);
  }

  @Override protected int menuItemId() {
    return R.id.nav_hello_world;
  }

  public static Intent newIntent(Context context) {
    return new Intent(context, HelloWorldActivity.class);
  }
}
