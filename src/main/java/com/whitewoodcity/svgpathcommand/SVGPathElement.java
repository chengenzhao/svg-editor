package com.whitewoodcity.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

public sealed interface SVGPathElement permits CurveTo, LineTo, MoveTo, QuadraticTo, SmoothTo, TransitTo {
  String command();
  SimpleDoubleProperty x();
  SimpleDoubleProperty y();
  default double getX(){return this.x().get();}
  default double getY(){return this.y().get();}

  default SVGPathElement copyCoordinate(){
    return new MoveTo(getX(), getY());
  }
}
