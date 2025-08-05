package com.whitewoodcity.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

public record LineTo(SimpleDoubleProperty x, SimpleDoubleProperty y) implements SVGPathCommand {
}
