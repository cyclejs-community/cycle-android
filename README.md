Cycle.js Android
=========

[![Build Status](https://travis-ci.org/cyclejs-community/cycle-android.svg?branch=master)](https://travis-ci.org/cyclejs-community/cycle-android)

A Proof of Concept that ports the [Cycle.js](http://cycle.js.org) reactive concepts to Android.
It currently uses [Anvil](https://github.com/zserge/anvil) for incremental UI updates, [Retrofit](https://github.com/square/retrofit)
for HTTP requests and, obviously, [RxJava](https://github.com/ReactiveX/RxJava) for the Observables
implementation.

It's still alpha state, but already demonstrates that it can be achieved.
The repo has a few demos: The classic Hello World, Counter, Github search and BMI Calculator.
All have been ported from the Cycle.js [Examples repository](https://github.com/cyclejs/examples).

# Usage

`build.gradle`

```groovy
compile 'com.felipecsl:cycle-android:0.2.0'
```

`MyActivity.java`

```java
public class MyActivity extends AppCompatActivity {
   @Override protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_sample);
     app = (ViewGroup) findViewById(R.id.app);
     DomSource domSource = new DomSource(DomDriver.makeDomDriver(app));
     HttpSource httpSource = new HttpSource(HttpDriver.makeHttpDriver());
     Cycle.run(this::main, domSource, httpSource);
   }

   private Sinks main(Sources sources) {
     DomSink domSink = DomSink.create(sources.dom()
       .select(R.id.editName)
       .events("input")
       .map(ev -> ev.<EditText>view().getText().toString())
       .startWith("")
       .map(name -> (Anvil.Renderable) () ->
           xml(R.layout.vtree_helloworld, () ->
               withId(R.id.txtHelloWorld, () ->
                   text("Hello, " + name)))));

       return Sinks.create(domSink);
   }
}
```

See the [Sample app](https://github.com/cyclejs-community/cycle-android/tree/master/sample/src/main/java/org/js/cycle/android/sample) for more examples.


License
-------

    Copyright (C) 2016 Felipe Lima

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



