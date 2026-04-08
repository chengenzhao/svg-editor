package com.whitewoodcity.svgeditor;

import com.whitewoodcity.control.NumberField;
import com.whitewoodcity.fxgl.vectorview.JVG;
import com.whitewoodcity.fxgl.vectorview.JVGLayer;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LeftColumn extends VBox {

  private final Button zoomIn = new Button("+");
  private final Button zoomOut = new Button("-");
  private final NumberField factor = new NumberField(1, 2);
  private final CheckBox globalZoom = new CheckBox();

  public LeftColumn() {
    var hbox = new HBox(zoomIn, zoomOut);
    hbox.setPadding(new Insets(5,10,5,10));
    hbox.setSpacing(5);
    hbox.setAlignment(Pos.BASELINE_LEFT);

    factor.setText("1.1");
    factor.setPrefWidth(30);
    var hbox1 = new HBox(new Label("* or /"),factor);
    hbox1.paddingProperty().bind(hbox.paddingProperty());
    hbox1.spacingProperty().bind(hbox.spacingProperty());
    hbox1.alignmentProperty().bind(hbox.alignmentProperty());

    var hbox2 = new HBox(new Label("Global change:"), globalZoom);
    hbox2.paddingProperty().bind(hbox.paddingProperty());
    hbox2.spacingProperty().bind(hbox.spacingProperty());
    hbox2.alignmentProperty().bind(hbox.alignmentProperty());
    globalZoom.setSelected(false);

    this.getChildren().addAll(hbox,hbox1,hbox2);

    zoomIn.setOnAction(_ -> {
      var f = factor.getDouble();
      if (globalZoom.isSelected()) {
        zoom(f);
      } else {
        zoomCurrentNode(f);
      }
    });
    zoomOut.setOnAction(_ -> {
      var f = 1.0/factor.getDouble();
      if (globalZoom.isSelected()) {
        zoom(f);
      } else {
        zoomCurrentNode(f);
      }
    });
  }

  public void zoom(double factor){
    var jvg = new JVG(JVG.toJson(SVGEditor.getAppCast().center.getChildren()).toString());
    var xy = jvg.getXY();
    for(var node:SVGEditor.getAppCast().center.getChildren()){
      if(node instanceof JVGLayer layer){
        zoom(xy, layer, factor);
      }
    }
    if(SVGEditor.getAppCast().rightTree.currentNodeInPane() instanceof JVGLayer layer)
      SVGEditor.getAppCast().bottom.fillParameters.updateNBind(layer);
  }

  public void zoomCurrentNode(double factor){
    var node = SVGEditor.getAppCast().rightTree.currentNodeInPane();
    zoom(node, factor);
    if(node instanceof JVGLayer layer)
      SVGEditor.getAppCast().bottom.fillParameters.updateNBind(layer);
  }

  public void zoom(Node node, double factor){
    switch (node){
      case ImageView view -> {
        view.setFitWidth(view.getFitWidth() * factor);
        view.setFitHeight(view.getFitHeight() * factor);
      }
      case JVGLayer layer -> zoom(layer, factor);
      default -> {}
    }
  }

  public void zoom(Point2D anchorPoint, JVGLayer layer, double factor){
    layer.fillProperty().unbind();
    layer.trim(anchorPoint)
      .zoom(factor)
      .move(anchorPoint);
    SVGEditor.getAppCast().updateSVGPath(layer);
  }

  public void zoom(JVGLayer layer, double factor){
    var coordinate = layer.getMinXY();
    zoom(coordinate, layer, factor);
  }

  public Button getZoomIn() {
    return zoomIn;
  }

  public Button getZoomOut() {
    return zoomOut;
  }

  public double getFactor() {
    return factor.getDouble();
  }
}
