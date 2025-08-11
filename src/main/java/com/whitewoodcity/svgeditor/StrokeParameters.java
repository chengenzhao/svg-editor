package com.whitewoodcity.svgeditor;

import atlantafx.base.controls.Spacer;
import com.whitewoodcity.control.NumberField;
import module javafx.controls;

public class StrokeParameters extends SVGEditorHeader {
  private NumberField strokeWidth = new NumberField(0,100);
  private ColorPicker stroke = new ColorPicker();

  public StrokeParameters(){
    super();
    strokeWidth.setText("1");
    strokeWidth.setPrefWidth(50);

    stroke.setValue(Color.BLACK);

    this.getChildren().addAll(new Label("Stroke Width:"),strokeWidth,new Spacer(10),
      new Label("Stroke:"), stroke);
  }

  public NumberField getStrokeWidth() {
    return strokeWidth;
  }

  public ColorPicker getStroke() {
    return stroke;
  }
}
