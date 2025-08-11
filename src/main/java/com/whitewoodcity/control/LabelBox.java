package com.whitewoodcity.control;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class LabelBox extends HBox {
  private String filePath;
  private LabelBox father;
  public LabelBox() {
  }

  public LabelBox(double v) {
    super(v);
  }

  public LabelBox(Node... nodes) {
    super(nodes);
  }

  public LabelBox(double v, Node... nodes) {
    super(v, nodes);
  }

  public String getLabelString(){
    return getLabel().getText();
  }

  public Label getLabel(){
    return (Label)this.getChildren().getFirst();
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public LabelBox getFather() {
    return father;
  }

  public void setFather(LabelBox father) {
    this.father = father;
  }
}
