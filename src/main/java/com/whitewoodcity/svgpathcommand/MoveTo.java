package com.whitewoodcity.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

public record MoveTo(SimpleDoubleProperty x, SimpleDoubleProperty y) implements SVGPathElement {
  @Override
  public String command() {
    return "M";
  }

  public MoveTo(double x, double y) {
    this(new SimpleDoubleProperty(x), new SimpleDoubleProperty(y));
  }
}
