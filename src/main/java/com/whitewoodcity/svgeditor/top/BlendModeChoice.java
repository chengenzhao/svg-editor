package com.whitewoodcity.svgeditor.top;

import com.whitewoodcity.svgeditor.SVGEditorHeader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;

public class BlendModeChoice extends SVGEditorHeader {
  ChoiceBox<BlendMode> choiceBox = new ChoiceBox<>();

  public BlendModeChoice() {

    choiceBox.getItems().add(null);
    choiceBox.getItems().addAll(BlendMode.values());

    this.getChildren().addAll(new Label("Blend Mode"),choiceBox);
  }

  public ChoiceBox<BlendMode> getChoiceBox() {
    return choiceBox;
  }
}
