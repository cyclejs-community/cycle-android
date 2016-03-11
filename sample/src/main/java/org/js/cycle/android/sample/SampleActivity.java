package org.js.cycle.android.sample;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;

import org.js.cycle.android.Cycle;
import org.js.cycle.android.Sinks;
import org.js.cycle.android.Sources;

public abstract class SampleActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
  protected DrawerLayout drawerLayout;
  protected NavigationView navigationView;
  protected Toolbar toolbar;
  protected ViewGroup app;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    app = (ViewGroup) findViewById(R.id.app);
    drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    navigationView = (NavigationView) findViewById(R.id.drawer);
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    //noinspection ConstantConditions
    actionBar.setDisplayShowTitleEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawerLayout.addDrawerListener(toggle);
    navigationView.setNavigationItemSelectedListener(this);
    toggle.syncState();

    Cycle.run(this::main, app);
  }

  protected abstract Sinks main(Sources sources);

  @Override public boolean onNavigationItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.nav_counter:
        startActivity(CounterActivity.newIntent(this));
        finish();
        break;
      case R.id.nav_hello_world:
        startActivity(HelloWorldActivity.newIntent(this));
        finish();
        break;
      default:
        return false;
    }
    return true;
  }
}
