package com.whitewoodcity.svgeditor;

import com.whitewoodcity.control.NumberField;
import com.whitewoodcity.javafx.jvg.JVG;
import com.whitewoodcity.javafx.jvg.JVGLayer;
import com.whitewoodcity.javafx.jvg.JVGPath;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;

public class LeftColumn extends VBox {

  private final Button zoomIn = new Button("*");
  private final Button zoomOut = new Button("/");
  private final Button up = new Button("↑");
  private final Button left = new Button("←");
  private final Button right = new Button("→");
  private final Button down = new Button("↓");
  private final NumberField factor = new NumberField(1, 2);
  private final NumberField movingDistance = new NumberField(0, 100);
  private final CheckBox globalChanged = new CheckBox();

  public LeftColumn() {
    var hbox = new HBox(zoomIn, zoomOut);
    hbox.setPadding(new Insets(5, 0, 5, 0));
    hbox.setSpacing(5);
    hbox.setAlignment(Pos.BASELINE_LEFT);

    factor.setText("1.1");
    factor.setPrefWidth(30);
    var hbox1 = new HBox(zoomIn, new Label("or"), zoomOut, factor);
    hbox1.paddingProperty().bind(hbox.paddingProperty());
    hbox1.spacingProperty().bind(hbox.spacingProperty());
    hbox1.setAlignment(Pos.CENTER);

    var borderpane = new BorderPane();
    var topBox = new HBox(up);
    topBox.setAlignment(Pos.CENTER);
    borderpane.setTop(topBox);
    var bottomBox = new HBox(down);
    bottomBox.setAlignment(Pos.CENTER);
    borderpane.setBottom(bottomBox);
    borderpane.setLeft(left);
    borderpane.setRight(right);

    movingDistance.setText("10.0");
    movingDistance.setPrefWidth(30);
    borderpane.setPadding(new Insets(10));

    borderpane.setCenter(movingDistance);

    var hbox5 = new HBox(new Label("Apply to allJVGlayers"), globalChanged);
    hbox5.paddingProperty().bind(hbox.paddingProperty());
    hbox5.spacingProperty().bind(hbox.spacingProperty());
    hbox5.alignmentProperty().bind(hbox.alignmentProperty());
    globalChanged.setSelected(false);

    this.getChildren().addAll(hbox, hbox1, borderpane, hbox5);

    zoomIn.setOnAction(_ -> zoom(getFactor(), globalChanged.isSelected()));
    zoomOut.setOnAction(_ -> zoom(1.0 / getFactor(), globalChanged.isSelected()));

    up.setOnAction(_ -> move(0, -movingDistance.getDouble(), globalChanged.isSelected()));
    down.setOnAction(_ -> move(0, movingDistance.getDouble(), globalChanged.isSelected()));
    left.setOnAction(_ -> move(-movingDistance.getDouble(), 0, globalChanged.isSelected()));
    right.setOnAction(_ -> move(movingDistance.getDouble(), 0, globalChanged.isSelected()));
  }

  public void move(double x, double y, boolean global) {
    if (SVGEditor.getAppCast().rightTree.currentNodeInPane() instanceof Shape shape) {
      shape.fillProperty().unbind();
    }

    if (global) {
      var jvg = new JVG(JVG.toJson(SVGEditor.getAppCast().center.getChildren()).toString());
      var xy = jvg.getXY();
      for (var node : SVGEditor.getAppCast().center.getChildren()) {
        if (node instanceof JVGLayer layer) {
          layer.move(x, y);
          layer.update();
        }
      }
    } else if (SVGEditor.getAppCast().rightTree.currentNodeInPane() instanceof JVGLayer svgl) {
      svgl.move(x, y);
      svgl.update();
    }

    if (SVGEditor.getAppCast().rightTree.currentNodeInPane() instanceof JVGLayer svgl) {
      SVGEditor.getAppCast().bottom.fillParameters.updateNBind(svgl);
    }
  }

  public void zoom(double factor, boolean global) {
    if (global) {
      zoom(factor);
    } else {
      zoomCurrentNode(factor);
    }
  }

  public void zoom(double factor) {
    var jvg = new JVG(JVG.toJson(SVGEditor.getAppCast().center.getChildren()).toString());
    var xy = jvg.getXY();
    for (var node : SVGEditor.getAppCast().center.getChildren()) {
      if (node instanceof JVGLayer layer) {
        zoom(xy, layer, factor);
      }
    }
    if (SVGEditor.getAppCast().rightTree.currentNodeInPane() instanceof JVGPath layer)
      SVGEditor.getAppCast().bottom.fillParameters.updateNBind(layer);
  }

  public void zoomCurrentNode(double factor) {
    var node = SVGEditor.getAppCast().rightTree.currentNodeInPane();
    zoom(node, factor);
    if (node instanceof JVGLayer layer)
      SVGEditor.getAppCast().bottom.fillParameters.updateNBind(layer);
  }

  public void zoom(Node node, double factor) {
    switch (node) {
      case ImageView view -> {
        view.setFitWidth(view.getFitWidth() * factor);
        view.setFitHeight(view.getFitHeight() * factor);
      }
      case JVGLayer layer -> zoom(layer, factor);
      case null, default -> {
      }
    }
  }

  public void zoom(Point2D anchorPoint, JVGLayer layer, double factor) {
    if (layer instanceof Shape shape)
      shape.fillProperty().unbind();
    layer.trim(anchorPoint)
      .zoom(factor)
      .move(anchorPoint);
    if (layer instanceof JVGPath jvgPath)
      SVGEditor.getAppCast().updateSVGPath(jvgPath);
  }

  public void zoom(JVGLayer layer, double factor) {
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

  public Button up() {
    return up;
  }

  public Button left() {
    return left;
  }

  public Button right() {
    return right;
  }

  public Button down() {
    return down;
  }

  public NumberField getMovingDistance() {
    return movingDistance;
  }

  public CheckBox getGlobalChanged() {
    return globalChanged;
  }
}
