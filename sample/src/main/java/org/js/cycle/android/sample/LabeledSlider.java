package org.js.cycle.android.sample;

import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.js.cycle.android.Component;
import org.js.cycle.android.DomSink;
import org.js.cycle.android.Props;
import org.js.cycle.android.Sinks;
import org.js.cycle.android.Sources;
import org.js.cycle.android.ValueSink;

import rx.Observable;
import trikita.anvil.Anvil;

import static trikita.anvil.BaseDSL.text;
import static trikita.anvil.DSL.editText;
import static trikita.anvil.DSL.id;
import static trikita.anvil.DSL.inputType;
import static trikita.anvil.DSL.linearLayout;
import static trikita.anvil.DSL.orientation;
import static trikita.anvil.DSL.textView;

public class LabeledSlider implements Component{
  @Override
  public Sinks create(Sources sources) {
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

  public static class LabeledSliderProps implements Props {
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
}
