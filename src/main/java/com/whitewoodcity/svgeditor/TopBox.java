package com.whitewoodcity.svgeditor;

import com.whitewoodcity.svgeditor.top.EffectParameters;
import com.whitewoodcity.svgeditor.top.FillParameters;
import com.whitewoodcity.svgeditor.top.PathElements;
import com.whitewoodcity.svgeditor.top.StrokeParameters;
import javafx.scene.layout.VBox;

public class TopBox extends VBox {

  public PathElements pathElements = new PathElements();
  public StrokeParameters strokeParameters = new StrokeParameters();
  public FillParameters fillParameters = new FillParameters();
  public EffectParameters effectParameters = new EffectParameters();

  public TopBox() {
    this.getChildren().addAll(pathElements, strokeParameters, fillParameters, effectParameters);

    pathElements.getZ().selectedProperty().addListener((_, _, _) -> SVGEditor2.getAppCast().updateSVGPath());
  }
}
