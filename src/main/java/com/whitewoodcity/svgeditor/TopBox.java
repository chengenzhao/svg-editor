package com.whitewoodcity.svgeditor;

import javafx.scene.layout.VBox;

public class TopBox extends VBox {

  public PathElements pathElements = new PathElements();
  public StrokeParameters strokeParameters = new StrokeParameters();
  public FillParameters fillParameters = new FillParameters();

  public TopBox() {
    this.getChildren().addAll(pathElements, strokeParameters, fillParameters);
  }
}
