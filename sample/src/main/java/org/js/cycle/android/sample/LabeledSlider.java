package org.js.cycle.android.sample;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

class LabeledSlider extends LinearLayout {
  @IdRes static final int NUMBER_PICKER_VIEW_ID = R.id.labeled_slider_picker;
  private final TextView label;
  private final NumberPicker picker;

  public LabeledSlider(Context context) {
    this(context, null);
  }

  public LabeledSlider(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LabeledSlider(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    LayoutInflater.from(context).inflate(R.layout.layout_labeled_slider, this);
    setOrientation(LinearLayout.VERTICAL);
    label = (TextView) findViewById(R.id.labeled_slider_label);
    picker = (NumberPicker) findViewById(R.id.labeled_slider_picker);
  }

  void setLabel(String newLabel) {
    label.setText(newLabel);
    // TODO: use the BmiComponent parameters to set the min/max here
    picker.setMinValue(0);
    picker.setMaxValue(200);
  }
}
