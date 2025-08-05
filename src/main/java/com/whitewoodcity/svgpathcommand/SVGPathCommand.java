package com.whitewoodcity.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

public sealed interface SVGPathCommand permits CurveTo, LineTo, MoveTo, QuadraticTo, SmoothTo, TransitTo {
  SimpleDoubleProperty x();
  SimpleDoubleProperty y();
}
