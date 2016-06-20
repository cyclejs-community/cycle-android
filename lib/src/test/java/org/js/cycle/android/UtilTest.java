package org.js.cycle.android;

import android.view.View;
import android.view.ViewGroup;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UtilTest {
  @Test public void emptyFilter() {
    View view = mock(View.class);
    assertThat(Util.viewMatchesSelector(view, Collections.emptyList())).isTrue();
  }

  @Test public void singleFilter() {
    View view = mock(View.class);
    when(view.getId()).thenReturn(2);
    assertThat(Util.viewMatchesSelector(view, Collections.singletonList(2))).isTrue();
  }

  @Test public void nonMatchingFilter() {
    View view = mock(View.class);
    when(view.getId()).thenReturn(1);
    assertThat(Util.viewMatchesSelector(view, Collections.singletonList(2))).isFalse();
  }

  @Test public void multipleFilters() {
    ViewGroup parentView = mock(ViewGroup.class);
    View view = mock(View.class);
    when(view.getId()).thenReturn(2);
    when(view.getParent()).thenReturn(parentView);
    when(parentView.getId()).thenReturn(3);
    assertThat(Util.viewMatchesSelector(view, Arrays.asList(3, 2))).isTrue();
    assertThat(Util.viewMatchesSelector(view, Arrays.asList(2, 3))).isFalse();
    assertThat(Util.viewMatchesSelector(view, Arrays.asList(1, 2))).isFalse();
    assertThat(Util.viewMatchesSelector(view, Arrays.asList(4, 3, 2))).isFalse();
  }
}