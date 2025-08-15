package com.whitewoodcity.svgeditor.top;

import com.whitewoodcity.svgeditor.SVGEditorHeader;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.shape.Rectangle;

public class EffectParameters extends SVGEditorHeader {
  private ChoiceBox<String> choiceBox = new ChoiceBox<>();

  private Node node;

  public EffectParameters() {

    this.choiceBox = choiceBox;

    choiceBox.getItems().addAll(null, "GaussianBlur");

    this.getChildren().add(choiceBox);

    choiceBox.getSelectionModel().selectedItemProperty().addListener((_,_,newV)->{
      switch (newV){
        case null -> node.setEffect(null);
        case "GaussianBlur" -> node.setEffect(new GaussianBlur());
        default -> {}
      }
    });
  }

  public void setNode(Node node) {
    this.node = node;
    switch (node.getEffect()){
      case null -> choiceBox.setValue(null);
      case GaussianBlur gaussianBlur-> choiceBox.setValue("GaussianBlur");
      default -> choiceBox.setValue(null);
    }
  }
}
