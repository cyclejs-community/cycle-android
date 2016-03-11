package com.cycleandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rx.Observable;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    ViewGroup app = (ViewGroup) findViewById(R.id.app);
    setSupportActionBar(toolbar);

    Cycle.run(this::main, app);
  }

  private Sinks main(Sources sources) {
    Observable<Integer> actionStream = Observable.merge(
        sources.dom().select(R.id.btnIncrement).events("click").map(ev -> 1),
        sources.dom().select(R.id.btnDecrement).events("click").map(ev -> -1));

    return Sinks.create(actionStream
        .startWith(0)
        .scan((x, y) -> x + y)
        .map(String::valueOf)
        .map(c -> {
          View tree = LayoutInflater.from(this).inflate(R.layout.vtree_main, null);
          TextView txtCounter = (TextView) tree.findViewById(R.id.txtCount);
          txtCounter.setText(c);
          return tree;
        }));
  }
}
