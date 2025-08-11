package com.whitewoodcity.svgeditor;

import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class PathElements extends SVGEditorHeader {

  RadioButton m = new RadioButton("M");
  RadioButton l = new RadioButton("L");
  RadioButton t = new RadioButton("T");
  RadioButton s = new RadioButton("S");
  RadioButton q = new RadioButton("Q");
  RadioButton c = new RadioButton("C");
  RadioButton z = new RadioButton("Z");

  public PathElements() {
    super();
    this.getChildren().addAll(m,l,t,s,q,c,z);

    ToggleGroup radioGroup = new ToggleGroup();

    m.setToggleGroup(radioGroup);
    l.setToggleGroup(radioGroup);
    t.setToggleGroup(radioGroup);
    s.setToggleGroup(radioGroup);
    q.setToggleGroup(radioGroup);
    c.setToggleGroup(radioGroup);
    z.setToggleGroup(radioGroup);

    l.setSelected(true);
  }

  public RadioButton getM() {
    return m;
  }

  public RadioButton getL() {
    return l;
  }

  public RadioButton getT() {
    return t;
  }

  public RadioButton getS() {
    return s;
  }

  public RadioButton getQ() {
    return q;
  }

  public RadioButton getC() {
    return c;
  }

  public RadioButton getZ() {
    return z;
  }
}
