package org.js.cycle.android.sample;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import org.js.cycle.android.DomSink;
import org.js.cycle.android.Sinks;
import org.js.cycle.android.Sources;

import rx.Observable;
import rx.functions.Action0;

import static trikita.anvil.BaseDSL.withId;
import static trikita.anvil.BaseDSL.xml;
import static trikita.anvil.DSL.text;

public class BMIActivity extends SampleActivity {

  private class State {
     public Integer weight;
     public Integer height;
     public Integer bmi;

    public State(Integer weight, Integer height, Integer bmi) {
      this.weight = weight;
      this.height = height;
      this.bmi = bmi;
    }
  }

  @Override protected Sinks main(Sources sources) {
    Observable<Integer> weight$ = sources.dom()
        .select(R.id.editWeight)
        .events("input")
        .map(ev -> Integer.valueOf(ev.<EditText>view().getText().toString()));

    Observable<Integer> height$ = sources.dom()
        .select(R.id.editHeight)
        .events("input")
        .map(ev -> Integer.valueOf(ev.<EditText>view().getText().toString()));


    Observable<State> state$ = Observable.combineLatest(
      weight$.startWith(70),
      height$.startWith(170),
      (Integer weight, Integer height) -> {
        final Double heightMeters = height.doubleValue() * 0.01;
        final Double bmi = weight.doubleValue() / (heightMeters * heightMeters);
        return new State(weight, height, bmi.intValue());
      });

    DomSink domSink = DomSink.create(
        state$
        .map((State state) -> (Action0) () ->
            xml(R.layout.vtree_bmi, () -> {
                withId(R.id.txtWeight, () ->
                        text("Weight " + state.weight + "kg"));
                withId(R.id.txtHeight, () ->
                        text("Height " + state.height + "cm" ));
                withId(R.id.txtBMI, () ->
                        text("BMI is " + state.bmi));

            })));

    return Sinks.create(domSink);
  }

  @Override protected int menuItemId() {
    return R.id.nav_bmi;
  }

  public static Intent newIntent(Context context) {
    return new Intent(context, BMIActivity.class);
  }
}
