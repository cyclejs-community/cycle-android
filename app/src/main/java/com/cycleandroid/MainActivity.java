package com.cycleandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

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
    Func1<String, View> vtree = c -> {
      View tree = LayoutInflater.from(this).inflate(R.layout.vtree_main, null);
      TextView txtCounter = (TextView) tree.findViewById(R.id.txtCounter);
      txtCounter.setText(c);
      return tree;
    };
    return Sinks.create(Observable.interval(1, 1, TimeUnit.SECONDS)
        .map(String::valueOf)
        .map(vtree)
        .observeOn(AndroidSchedulers.mainThread()));
  }
}
