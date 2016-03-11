package org.js.cycle.android.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import org.js.cycle.android.Sinks;
import org.js.cycle.android.Sources;

import rx.functions.Action0;

import static trikita.anvil.BaseDSL.text;
import static trikita.anvil.BaseDSL.withId;
import static trikita.anvil.BaseDSL.xml;

public class HelloWorldActivity extends SampleActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    navigationView.getMenu().findItem(R.id.nav_hello_world).setChecked(true);
  }

  @Override protected Sinks main(Sources sources) {
    return Sinks.create(sources.dom()
        .select(R.id.editName)
        .events("input")
        .map(ev -> ev.<EditText>view().getText().toString())
        .startWith("")
        .map(name -> (Action0) () ->
            xml(R.layout.vtree_helloworld, () ->
                withId(R.id.txtHelloWorld, () ->
                    text(new StringBuilder().append("Hello, ").append(name))))));
  }

  public static Intent newIntent(Context context) {
    return new Intent(context, HelloWorldActivity.class);
  }
}
