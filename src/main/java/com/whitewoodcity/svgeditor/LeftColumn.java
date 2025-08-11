package com.whitewoodcity.svgeditor;

import com.whitewoodcity.control.NumberField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LeftColumn extends VBox {

  private Button zoomIn = new Button("+");
  private Button zoomOut = new Button("-");
  private NumberField factor = new NumberField(1, 2);

  public LeftColumn() {
    var hbox = new HBox(zoomIn, zoomOut);
    hbox.setPadding(new Insets(5,0,5,10));
    hbox.setSpacing(10);
    hbox.setAlignment(Pos.BASELINE_LEFT);

    factor.setText("1.1");
    factor.setPrefWidth(30);
    var hbox1 = new HBox(new Label("* or /"),factor);
    hbox1.paddingProperty().bind(hbox.paddingProperty());
    hbox1.spacingProperty().bind(hbox.spacingProperty());
    hbox1.alignmentProperty().bind(hbox.alignmentProperty());

    this.getChildren().addAll(hbox,hbox1);
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
