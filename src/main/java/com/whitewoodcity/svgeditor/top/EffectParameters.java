package com.whitewoodcity.svgeditor.top;

import module java.base;
import atlantafx.base.controls.Spacer;
import com.whitewoodcity.control.NumberField;
import com.whitewoodcity.svgeditor.SVGEditorHeader;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.shape.Rectangle;

public class EffectParameters extends SVGEditorHeader {
  private ChoiceBox<String> choiceBox = new ChoiceBox<>();

  private Node node;

  private List<NumberField> zoomedFields = new ArrayList<>();

  public EffectParameters() {

    this.choiceBox = choiceBox;

    choiceBox.getItems().addAll(null, "GaussianBlur");

    this.getChildren().addAll(choiceBox, new Label("Effect"));

    choiceBox.getSelectionModel().selectedItemProperty().addListener((_,_,newV)->{
      switch (newV){
        case null -> {
          node.setEffect(null);
          this.getChildren().subList(2,this.getChildren().size()).clear();
          zoomedFields.clear();
        }
        case "GaussianBlur" -> {
          if(node.getEffect() == null)
            node.setEffect(new GaussianBlur());
          var effect = (GaussianBlur)node.getEffect();
          this.getChildren().subList(2,this.getChildren().size()).clear();
          var radius = new NumberField(10000);
          zoomedFields.add(radius);
          radius.setPrefWidth(40);
          radius.setText(effect.getRadius()+"");
          effect.radiusProperty().bind(radius.textProperty().map(t -> Double.parseDouble(t)));
          this.getChildren().addAll(new Spacer(10),radius, new Label("Radius"));
        }
        default -> {}
      }
    });
  }

  public void setNode(Node node) {
    this.node = node;
    switch (node.getEffect()){
      case null -> choiceBox.setValue(null);
      case GaussianBlur gaussianBlur-> {
        choiceBox.setValue("GaussianBlur");

        var effect = (GaussianBlur)node.getEffect();
        this.getChildren().subList(2,this.getChildren().size()).clear();
        var radius = new NumberField(10000);
        zoomedFields.add(radius);
        radius.setPrefWidth(40);
        radius.setText(effect.getRadius()+"");
        effect.radiusProperty().bind(radius.textProperty().map(t -> Double.parseDouble(t)));
        this.getChildren().addAll(new Spacer(10),radius, new Label("Radius"));
      }
      default -> choiceBox.setValue(null);
    }
  }

  public void zoomIn(double factor){
    zoomedFields.forEach(e -> e.setText(e.getDouble() * factor +""));
  }

  public void zoomOut(double factor){
    zoomedFields.forEach(e -> e.setText(e.getDouble() / factor +""));
  }
}
