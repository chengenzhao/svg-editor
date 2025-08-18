package com.whitewoodcity.svgeditor;

import com.whitewoodcity.control.NumberField;
import com.whitewoodcity.fxgl.vectorview.SVGLayer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class LeftColumn extends VBox {

  private Button zoomIn = new Button("+");
  private Button zoomOut = new Button("-");
  private NumberField factor = new NumberField(1, 2);

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

    this.getChildren().addAll(hbox,hbox1);

    zoomIn.setOnAction(_ -> {
      var node = SVGEditor2.getAppCast().rightTree.currentNodeInPane();
      switch (node){
        case ImageView view -> {
          view.setFitWidth(view.getFitWidth() * factor.getDouble());
          view.setFitHeight(view.getFitHeight() * factor.getDouble());
        }
        case SVGLayer path -> {
          var coordinate = path.getMinXY();
          path.trim(coordinate);
          path.zoom(factor.getDouble());
          path.move(coordinate);
          SVGEditor2.getAppCast().updateSVGPath();
        }
        default -> {}
      }
    });
    zoomOut.setOnAction(_ -> {
      var node = SVGEditor2.getAppCast().rightTree.currentNodeInPane();
      switch (node){
        case ImageView view -> {
          view.setFitWidth(view.getFitWidth() / factor.getDouble());
          view.setFitHeight(view.getFitHeight() / factor.getDouble());
        }
        case SVGLayer path -> {
          var coordinate = path.getMinXY();
          path.trim(coordinate);
          path.zoom(1.0/factor.getDouble());
          path.move(coordinate);
          SVGEditor2.getAppCast().updateSVGPath();
        }
        default -> {}
      }
    });
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
