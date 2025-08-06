package com.whitewoodcity.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

public record CurveTo(SimpleDoubleProperty x1, SimpleDoubleProperty y1, SimpleDoubleProperty x2, SimpleDoubleProperty y2, SimpleDoubleProperty x, SimpleDoubleProperty y) implements SVGPathCommand {
  @Override
  public String command() {
    return "C";
  }

  public double getX1() {
    return x1.get();
  }

  public double getY1() {
    return y1.get();
  }

  public double getX2() {
    return x2.get();
  }

  public double getY2() {
    return y2.get();
  }
}
