package com.whitewoodcity.svgeditor;

import com.whitewoodcity.control.NumberField;
import module javafx.controls;

public class StrokeParameters extends HBox {
  private NumberField strokeWidth = new NumberField(0,100);

  public StrokeParameters(){
    this.setSpacing(10);
    this.setPadding(new Insets(5,0,5,10));
    this.setAlignment(Pos.BASELINE_LEFT);
    strokeWidth.setText("1");
    strokeWidth.setPrefWidth(50);

    this.getChildren().addAll(new Label("Stroke Width:"),strokeWidth);
  }

  public NumberField getStrokeWidth() {
    return strokeWidth;
  }
}
