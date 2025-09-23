package com.whitewoodcity.svgeditor.top;

import com.whitewoodcity.svgeditor.SVGEditorHeader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class OpacityParameter extends SVGEditorHeader {
  private final Slider slider = new Slider(0,1.0,1);

  public OpacityParameter() {
    this.getChildren().addAll(new Label("Opacity:"),slider);
  }

  public Slider getSlider() {
    return slider;
  }
}
