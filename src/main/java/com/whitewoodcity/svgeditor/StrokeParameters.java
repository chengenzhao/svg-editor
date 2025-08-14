package com.whitewoodcity.svgeditor;

import atlantafx.base.controls.Spacer;
import com.whitewoodcity.control.NumberField;
import module javafx.controls;

public class StrokeParameters extends SVGEditorHeader {
  private NumberField strokeWidth = new NumberField(0, 100);
  private ColorPicker stroke = new ColorPicker();

  public StrokeParameters() {
    super();
    strokeWidth.setText("1");
    strokeWidth.setMinWidth(40);

    stroke.setValue(Color.BLACK);

    this.getChildren().addAll(
      stroke, new Label("Stroke"), new Spacer(10),
      strokeWidth, new Label("Stroke Width"), new Spacer(10)
    );
  }

  public NumberField getStrokeWidth() {
    return strokeWidth;
  }

  public ColorPicker getStroke() {
    return stroke;
  }
}
