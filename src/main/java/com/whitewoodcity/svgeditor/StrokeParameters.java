package com.whitewoodcity.svgeditor;

import com.whitewoodcity.control.NumberField;
import module javafx.controls;

public class StrokeParameters extends SVGEditorHeader {
  private NumberField strokeWidth = new NumberField(0,100);

  public StrokeParameters(){
    super();
    strokeWidth.setText("1");
    strokeWidth.setPrefWidth(50);

    this.getChildren().addAll(new Label("Stroke Width:"),strokeWidth);
  }

  public NumberField getStrokeWidth() {
    return strokeWidth;
  }
}
