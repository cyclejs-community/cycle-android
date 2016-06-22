package org.js.cycle.android.sample;

import android.content.Context;
import android.content.Intent;

import org.js.cycle.android.DomSink;
import org.js.cycle.android.PropsSource;
import org.js.cycle.android.Sinks;
import org.js.cycle.android.Sources;

import rx.Observable;

import static org.js.cycle.android.sample.BmiComponent.Output;
import static org.js.cycle.android.sample.BmiComponent.Properties;
import static trikita.anvil.BaseDSL.attr;
import static trikita.anvil.BaseDSL.text;
import static trikita.anvil.BaseDSL.withId;
import static trikita.anvil.BaseDSL.xml;

public class Foo extends SampleActivity {
  private static final PropsSource HEIGHT_PROPERTIES = new PropsSource(
      Observable.just(new Properties("Height", "cm", 140, 170, 210, R.id.labeled_slider_height)));
  private static final PropsSource WEIGHT_PROPERTIES = new PropsSource(
      Observable.just(new Properties("Weight", "kg", 40, 70, 140, R.id.labeled_slider_weight)));

  @Override protected Sinks main(Sources sources) {
    Sinks weightSinks = new BmiComponent().create(Sources.create(sources.dom(), WEIGHT_PROPERTIES));
    Sinks heightSinks = new BmiComponent().create(Sources.create(sources.dom(), HEIGHT_PROPERTIES));
    Observable<Bmi> bmi$ = Observable.combineLatest(
        weightSinks.findStreamOrThrow(BmiComponent.VALUE_SINK_NAME),
        heightSinks.findStreamOrThrow(BmiComponent.VALUE_SINK_NAME),
        this::calculateBmi);
    DomSink domSink = DomSink.create(bmi$.map(bmi -> () ->
        xml(R.layout.activity_bmi, () -> {
          withId(R.id.text_bmi, () ->
              text("BMI: " + bmi.bmi));
          withId(R.id.labeled_slider_height, () ->
              attr((v, newValue, oldValue) ->
                  ((LabeledSlider) v).setLabel(newValue), bmi.heightLabel));
          withId(R.id.labeled_slider_weight, () ->
              attr((v, newValue, oldValue) ->
                  ((LabeledSlider) v).setLabel(newValue), bmi.weightLabel));
        })));
    return Sinks.create(domSink);
  }

  @Override protected int menuItemId() {
    return R.id.nav_bmi;
  }

  private Bmi calculateBmi(Output weight, Output height) {
    double doubleWeight = ((Integer) weight.value).doubleValue();
    double doubleHeight = ((Integer) height.value).doubleValue();
    double heightMeters = doubleHeight * 0.01;
    int bmi = (int) (doubleWeight / (heightMeters * heightMeters));
    return new Bmi(weight.label, height.label, bmi);
  }

  static Intent newIntent(Context context) {
    return new Intent(context, Foo.class);
  }

  private static class Bmi {
    final String weightLabel;
    final String heightLabel;
    final int bmi;

    Bmi(String weightLabel, String heightLabel, int bmi) {
      this.weightLabel = weightLabel;
      this.heightLabel = heightLabel;
      this.bmi = bmi;
    }
  }
}
