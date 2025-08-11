package com.whitewoodcity.svgeditor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public abstract class SVGEditorHeader extends HBox {
  public SVGEditorHeader() {
    this.setSpacing(3);
    this.setPadding(new Insets(5,0,5,10));
    this.setAlignment(Pos.BASELINE_LEFT);
  }
}
