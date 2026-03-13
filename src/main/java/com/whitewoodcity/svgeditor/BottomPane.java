package com.whitewoodcity.svgeditor;

import com.whitewoodcity.svgeditor.bottom.FillParameters;
import com.whitewoodcity.svgeditor.bottom.StrokeParameters;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class BottomPane extends FlowPane {
  public StrokeParameters strokeParameters = new StrokeParameters();
  public FillParameters fillParameters = new FillParameters();

  public BottomPane() {
    this.getChildren().addAll(new VBox(strokeParameters, fillParameters));
    fillParameters.prefWidthProperty().bind(this.widthProperty());
  }
}
