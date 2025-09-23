package com.whitewoodcity.svgeditor.top;

import module java.base;
import atlantafx.base.controls.Spacer;
import com.whitewoodcity.control.NumberField;
import com.whitewoodcity.svgeditor.SVGEditorHeader;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;

public class EffectParameters extends SVGEditorHeader {
  private final ChoiceBox<String> choiceBox = new ChoiceBox<>();

  private Node node;

  private final List<NumberField> zoomedFields = new ArrayList<>();

  public EffectParameters() {

    choiceBox.getItems().addAll(null, "GaussianBlur");

    this.getChildren().addAll(new Label("Effect"),choiceBox);

    choiceBox.getSelectionModel().selectedItemProperty().addListener((_,_,newV)->{
      switch (newV){
        case null -> {
          node.setEffect(null);
          this.getChildren().subList(2,this.getChildren().size()).clear();
        }
        case "GaussianBlur" -> {
          if(node.getEffect() == null)
            node.setEffect(new GaussianBlur());
          layout(node.getEffect());
        }
        default -> {}
      }
    });
  }

  public void setNode(Node node) {
    this.node = node;
    zoomedFields.clear();
    switch (node.getEffect()){
      case null -> choiceBox.setValue(null);
      case GaussianBlur gaussianBlur-> {
        choiceBox.setValue("GaussianBlur");
        layout(gaussianBlur);
      }
      default -> choiceBox.setValue(null);
    }
  }

//  public void zoomIn(double factor){
//    zoomedFields.forEach(e -> e.setText(e.getDouble() * factor +""));
//  }
//
//  public void zoomOut(double factor){
//    zoomedFields.forEach(e -> e.setText(e.getDouble() / factor +""));
//  }

  private void layout(Effect effect){
    switch (effect){
      case null -> {}
      case GaussianBlur gaussianBlur ->{
        this.getChildren().subList(2,this.getChildren().size()).clear();
        var radius = new NumberField(10000);
        zoomedFields.add(radius);
        radius.setPrefWidth(40);
        radius.setText(gaussianBlur.getRadius()+"");
        gaussianBlur.radiusProperty().bindBidirectional(radius.valueProperty());
        this.getChildren().addAll(new Spacer(10),radius, new Label("Radius"));
      }
      default -> {}
    }
  }

  public List<NumberField> getZoomedFields() {
    return zoomedFields;
  }
}
