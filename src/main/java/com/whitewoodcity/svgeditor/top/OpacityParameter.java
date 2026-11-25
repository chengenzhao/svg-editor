package com.whitewoodcity.svgeditor.top;

import com.whitewoodcity.svgeditor.SVGEditorHeader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class OpacityParameter extends SVGEditorHeader {
  private final Slider slider = new Slider(0,1.0,1);

  public OpacityParameter() {
    slider.setMajorTickUnit(0.1);
    slider.setShowTickMarks(true);
    slider.setShowTickLabels(true);
    this.setAlignment(Pos.TOP_LEFT);
    this.getChildren().addAll(new Label("Opacity(Preview only)"),slider);
  }

  public Slider getSlider() {
    return slider;
  }
}
