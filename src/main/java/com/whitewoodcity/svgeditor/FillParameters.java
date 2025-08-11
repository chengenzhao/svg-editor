package com.whitewoodcity.svgeditor;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class FillParameters extends SVGEditorHeader{
  private ColorPicker fill = new ColorPicker();

  public FillParameters() {
    super();

    fill.setValue(Color.TRANSPARENT);

    this.getChildren().addAll(fill,new Label("Fill"));
  }

  public ColorPicker getFill() {
    return fill;
  }
}
