package org.js.cycle.android;

public class Sources {
  private final DOM dom;

  private Sources(DOM dom) {
    this.dom = dom;
  }

  public static Sources create(DOM dom) {
    return new Sources(dom);
  }

  public DOM dom() {
    return dom;
  }
}
