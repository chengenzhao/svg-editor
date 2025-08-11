package com.whitewoodcity.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

public record LineTo(SimpleDoubleProperty x, SimpleDoubleProperty y) implements SVGPathElement {
  @Override
  public String command() {
    return "L";
  }

  public LineTo(double x, double y) {
    this(new SimpleDoubleProperty(x), new SimpleDoubleProperty(y));
  }
}
