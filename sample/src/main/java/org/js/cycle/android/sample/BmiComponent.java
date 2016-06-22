package org.js.cycle.android.sample;

import android.support.annotation.IdRes;
import android.widget.NumberPicker;

import org.js.cycle.android.Component;
import org.js.cycle.android.ComponentProperties;
import org.js.cycle.android.Sink;
import org.js.cycle.android.Sinks;
import org.js.cycle.android.Sources;

import rx.Observable;

class BmiComponent implements Component {
  static final String VALUE_SINK_NAME = "BMIComponent_OUTPUT";

  @Override public Sinks create(Sources sources) {
    Properties p = sources.props()
        .observable()
        .cast(Properties.class)
        .toBlocking()
        .first();
    Observable<Output> value$ = sources.dom()
        .select(p.viewId)
        .select(LabeledSlider.NUMBER_PICKER_VIEW_ID)
        .events("change")
        .map(ev -> ev.<NumberPicker>view().getValue())
        .startWith(p.initial)
        .map(i -> Math.min(i, p.max))
        .map(i -> Math.max(i, p.min))
        .map(i -> new Output(p.label + ": " + i + p.unit, i));
    return Sinks.create(Sink.Factory.create(VALUE_SINK_NAME, value$));
  }

  static class Properties implements ComponentProperties {
    final String label;
    final String unit;
    final int initial;
    final int min;
    final int max;
    @IdRes final int viewId;

    Properties(String label, String unit, int min, int initial, int max, int viewId) {
      this.label = label;
      this.unit = unit;
      this.min = min;
      this.initial = initial;
      this.max = max;
      this.viewId = viewId;
    }
  }

  static class Output {
    final String label;
    final int value;

    Output(String label, int value) {
      this.label = label;
      this.value = value;
    }
  }
}
