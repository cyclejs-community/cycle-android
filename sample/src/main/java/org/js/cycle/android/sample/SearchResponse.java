package org.js.cycle.android.sample;

import java.util.Collections;
import java.util.List;

public class SearchResponse {
  int total_count;
  boolean incomplete_results;
  List<SearchResponseItem> items = Collections.emptyList();

  static class SearchResponseItem {
    long id;
    String name;
    String full_name;
  }
}
