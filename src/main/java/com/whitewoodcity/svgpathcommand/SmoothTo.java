package com.whitewoodcity.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

public record SmoothTo(SimpleDoubleProperty x2, SimpleDoubleProperty y2, SimpleDoubleProperty x, SimpleDoubleProperty y) implements SVGPathCommand {
  @Override
  public String command() {
    return "S";
  }

  public double getX2() {
    return x2.get();
  }

  public SimpleDoubleProperty x2Property() {
    return x2;
  }

  public double getY2() {
    return y2.get();
  }

  public SimpleDoubleProperty y2Property() {
    return y2;
  }
}
