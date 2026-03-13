package com.whitewoodcity.svgeditor.bottom;

import module javafx.controls;
import atlantafx.base.controls.Spacer;
import com.whitewoodcity.control.NumberField;
import com.whitewoodcity.svgeditor.SVGEditorHeader;

public class StrokeParameters extends SVGEditorHeader {
  private final NumberField strokeWidth = new NumberField(0, 100);
  private final ColorPicker stroke = new ColorPicker();

  public StrokeParameters() {
    super();
    strokeWidth.setText("1");
    strokeWidth.setPrefWidth(40);

    stroke.setValue(Color.BLACK);

    this.getChildren().addAll(
      new Label("Stroke"), stroke,new Spacer(10),
      new Label("Stroke Width"), strokeWidth,new Spacer(10)
    );
  }

  public NumberField getStrokeWidth() {
    return strokeWidth;
  }

  public ColorPicker getStroke() {
    return stroke;
  }

}
