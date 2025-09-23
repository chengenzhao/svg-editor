package com.whitewoodcity.svgeditor;

import com.whitewoodcity.svgeditor.top.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TopBox extends VBox {

  public PathElements pathElements = new PathElements();
  public StrokeParameters strokeParameters = new StrokeParameters();
  public FillParameters fillParameters = new FillParameters();
  public EffectParameters effectParameters = new EffectParameters();
  public BlendModeChoice blendModeChoice = new BlendModeChoice();
  public OpacityParameter opacityParameter = new OpacityParameter();

  public TopBox() {
    this.getChildren().addAll(new HBox(pathElements, strokeParameters, fillParameters),new HBox( effectParameters,blendModeChoice,opacityParameter));

    pathElements.getZ().selectedProperty().addListener((_, _, _) -> SVGEditor.getAppCast().updateSVGPath());
  }
}
