package org.js.cycle.android.sample;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

import org.js.cycle.android.DomSink;
import org.js.cycle.android.PropsSource;
import org.js.cycle.android.Sinks;
import org.js.cycle.android.Sources;

import rx.Observable;
import trikita.anvil.Anvil;

import static trikita.anvil.DSL.linearLayout;
import static trikita.anvil.DSL.orientation;
import static trikita.anvil.DSL.text;
import static trikita.anvil.DSL.textView;

public class BMIActivity extends SampleActivity {

  @Override
  protected Sinks main(Sources sources) {
    final LabeledSlider labeledSlider =  new LabeledSlider();
    Sinks weightSinks = labeledSlider.create(
        Sources.create(
            sources.dom(),
            new PropsSource(Observable.just(new LabeledSlider.LabeledSliderProps("Weight", "kg", 40, 70, 140)))
        )
    );

    Sinks heightSinks = labeledSlider.create(
        Sources.create(
            sources.dom(),
            new PropsSource(Observable.just(new LabeledSlider.LabeledSliderProps("Height", "cm", 140, 170, 210)))
        )
    );

    Observable<Integer> bmi$ = Observable.combineLatest(
        weightSinks.value().stream(), heightSinks.value().stream(),
        (Integer weight, Integer height) -> {
          final Double heightMeters = height.doubleValue() * 0.01;
          final Double bmi = weight.doubleValue() / (heightMeters * heightMeters);
          return bmi.intValue();
        });

    DomSink domSink = DomSink.create(
        Observable.combineLatest(bmi$, weightSinks.dom().stream(), heightSinks.dom().stream(),
            (bmi, weightVtree, heightVtree) -> (Anvil.Renderable) () ->
                linearLayout(() -> {
                  orientation(LinearLayout.VERTICAL);
                  ((Anvil.Renderable) weightVtree).view();
                  ((Anvil.Renderable) heightVtree).view();
                  textView(() -> text("BMI: " + bmi));
                })

        )
    );

    return Sinks.create(domSink);
  }

  @Override
  protected int menuItemId() {
    return R.id.nav_bmi;
  }

  public static Intent newIntent(Context context) {
    return new Intent(context, BMIActivity.class);
  }
}
