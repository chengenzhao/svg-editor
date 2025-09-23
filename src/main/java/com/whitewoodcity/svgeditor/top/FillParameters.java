package com.whitewoodcity.svgeditor.top;

import com.whitewoodcity.svgeditor.SVGEditorHeader;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class FillParameters extends SVGEditorHeader {
  private ColorPicker fill = new ColorPicker();

  public FillParameters() {
    super();

    fill.setValue(Color.TRANSPARENT);

    this.getChildren().addAll(new Label("Fill"),fill);
  }

  public ColorPicker getFill() {
    return fill;
  }
}
