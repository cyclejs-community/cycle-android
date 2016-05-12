package org.js.cycle.android.sample;

import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.js.cycle.android.DomSink;
import org.js.cycle.android.Props;
import org.js.cycle.android.PropsSource;
import org.js.cycle.android.Sinks;
import org.js.cycle.android.Sources;
import org.js.cycle.android.ValueSink;

import rx.Observable;
import trikita.anvil.Anvil;

import static trikita.anvil.DSL.editText;
import static trikita.anvil.DSL.id;
import static trikita.anvil.DSL.inputType;
import static trikita.anvil.DSL.linearLayout;
import static trikita.anvil.DSL.max;
import static trikita.anvil.DSL.maxValue;
import static trikita.anvil.DSL.minValue;
import static trikita.anvil.DSL.numberPicker;
import static trikita.anvil.DSL.orientation;
import static trikita.anvil.DSL.seekBar;
import static trikita.anvil.DSL.text;
import static trikita.anvil.DSL.textView;
import static trikita.anvil.DSL.value;

public class BMIActivity extends SampleActivity {

  private class LabeledSliderProps implements Props {
    public final String label;
    public final String unit;
    public final int min;
    public final int initial;
    public final int max;

    public LabeledSliderProps(String label, String unit, int min, int initial, int max) {
      this.label = label;
      this.unit = unit;
      this.min = min;
      this.initial = initial;
      this.max = max;
    }
  }

  private Sinks LabeledSlider(Sources sources) {
    Observable<Integer> initialValue$ = sources.props()
        .observable()
        .map(props -> ((LabeledSliderProps) props).initial)
        .first();

    final int id = sources.hashCode(); // FIXME, think of better value for scoped ID without collision

    Observable<Integer> newValue$ = sources.dom()
        .select(id)
        .events("input")
        .map(ev ->  Integer.valueOf(ev.<EditText>view().getText().toString()));

    Observable<Integer> value$ = Observable.concat(initialValue$, newValue$); //initialValue$.concat(newValue$);


    Observable<Anvil.Renderable> vtree$ = Observable
        .combineLatest(
            sources.props().observable(),
            value$,
            (props, val) ->
                (Anvil.Renderable) () -> {
                  final LabeledSliderProps p = (LabeledSliderProps) props;
                  linearLayout(() -> {
                    orientation(LinearLayout.VERTICAL);
                    textView(() -> text(p.label + " " + val + p.unit));
// NumberPicker events not emitted (yet?)
//                    numberPicker(() -> {
//                      id(id);
//                      minValue(p.min);
//                      maxValue(p.max);
//                      value(val);
//                    });
                    editText(() -> {
                      inputType(InputType.TYPE_CLASS_NUMBER);
                      id(id);
                      text(Integer.toString(val));
                    });
                  });
                });

    return Sinks.create(ValueSink.create(value$), DomSink.create(vtree$));
  }

  @Override
  protected Sinks main(Sources sources) {
    Sinks weightSinks = LabeledSlider(
        Sources.create(
            sources.dom(),
            new PropsSource(Observable.just(new LabeledSliderProps("Weight", "kg", 40, 70, 140)))
        )
    );

    Sinks heightSinks = LabeledSlider(
        Sources.create(
            sources.dom(),
            new PropsSource(Observable.just(new LabeledSliderProps("Height", "cm", 140, 170, 210)))
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
