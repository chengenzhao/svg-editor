package com.whitewoodcity.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

public record QuadraticTo(SimpleDoubleProperty x1, SimpleDoubleProperty y1, SimpleDoubleProperty x, SimpleDoubleProperty y) implements SVGPathCommand { }
