package com.whitewoodcity.svgeditor;

import com.whitewoodcity.svgeditor.bottom.FillParameters;
import com.whitewoodcity.svgeditor.bottom.StrokeParameters;
import javafx.scene.layout.FlowPane;

public class BottomPane extends FlowPane {
  public StrokeParameters strokeParameters = new StrokeParameters();
  public FillParameters fillParameters = new FillParameters();

  public BottomPane() {
    this.getChildren().addAll(strokeParameters, fillParameters);
  }
}
