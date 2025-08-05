package com.whitewoodcity.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

//T(transit to) = smooth quadratic Bézier curveto (create a smooth quadratic Bézier curve)
public record TransitTo(SimpleDoubleProperty x, SimpleDoubleProperty y) implements SVGPathCommand {
}
