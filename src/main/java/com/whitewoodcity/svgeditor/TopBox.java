package com.whitewoodcity.svgeditor;

import com.whitewoodcity.svgeditor.top.*;
import javafx.scene.layout.VBox;

public class TopBox extends VBox {

  public PathElements pathElements = new PathElements();
  public StrokeParameters strokeParameters = new StrokeParameters();
  public FillParameters fillParameters = new FillParameters();
  public EffectParameters effectParameters = new EffectParameters();
  public BlendModeChoice blendModeChoice = new BlendModeChoice();

  public TopBox() {
    this.getChildren().addAll(pathElements, strokeParameters, fillParameters, effectParameters,blendModeChoice);

    pathElements.getZ().selectedProperty().addListener((_, _, _) -> SVGEditor2.getAppCast().updateSVGPath());
  }
}
