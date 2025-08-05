package com.whitewoodcity.svgeditor;

import module javafx.controls;
import org.jetbrains.annotations.NotNull;

public class SVGEditor2 extends Application {
  @Override
  public void start(Stage stage) {
    var vBox = new VBox();

    var hbox = getHBox();

    var pane = new Pane();
    pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
    pane.setPrefHeight(600);
    pane.setPrefWidth(800);

    vBox.getChildren().addAll(hbox, pane);
    vBox.setPrefWidth(pane.getPrefWidth());
    vBox.setPrefHeight(pane.getPrefHeight() + hbox.getHeight());

    Scene scene = new Scene(vBox, vBox.getPrefWidth(), vBox.getPrefHeight());
    vBox.prefWidthProperty().bind(scene.widthProperty());
    vBox.prefHeightProperty().bind(scene.heightProperty());
    stage.setTitle("Hello!");
    stage.setScene(scene);
    stage.show();
  }

  @NotNull
  private static HBox getHBox() {
    RadioButton m = new RadioButton("M");
    RadioButton l = new RadioButton("L");
    RadioButton t = new RadioButton("T");
    RadioButton s = new RadioButton("S");
    RadioButton q = new RadioButton("Q");
    RadioButton c = new RadioButton("C");

    ToggleGroup radioGroup = new ToggleGroup();

    m.setToggleGroup(radioGroup);
    l.setToggleGroup(radioGroup);
    t.setToggleGroup(radioGroup);
    s.setToggleGroup(radioGroup);
    q.setToggleGroup(radioGroup);
    c.setToggleGroup(radioGroup);

    var hbox = new HBox(m, l, t, s, q, c);

    hbox.setSpacing(3);
    hbox.setPadding(new Insets(10));

    m.setSelected(true);
    return hbox;
  }
}
