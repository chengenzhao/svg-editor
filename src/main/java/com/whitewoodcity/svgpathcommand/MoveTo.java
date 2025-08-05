package com.whitewoodcity.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

public record MoveTo(SimpleDoubleProperty x, SimpleDoubleProperty y) implements SVGPathCommand {
}
