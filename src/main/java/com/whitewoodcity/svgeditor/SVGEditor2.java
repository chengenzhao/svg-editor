package com.whitewoodcity.svgeditor;

import module javafx.controls;
import com.whitewoodcity.svgpathcommand.SVGPathCommand;

import java.util.ArrayList;
import java.util.List;

public class SVGEditor2 extends Application {

  RadioButton m = new RadioButton("M");
  RadioButton l = new RadioButton("L");
  RadioButton t = new RadioButton("T");
  RadioButton s = new RadioButton("S");
  RadioButton q = new RadioButton("Q");
  RadioButton c = new RadioButton("C");

  List<SVGPathCommand> pathCommands = new ArrayList<>();

  @Override
  public void start(Stage stage) {
    var vBox = new VBox();

    var hbox = getHBox();

    var pane = getPane();

    vBox.getChildren().addAll(hbox, pane);
    vBox.setPrefWidth(pane.getPrefWidth());
    vBox.setPrefHeight(pane.getPrefHeight() + hbox.getHeight());

    Scene scene = new Scene(vBox, vBox.getPrefWidth(), vBox.getPrefHeight());
    vBox.prefWidthProperty().bind(scene.widthProperty());
    vBox.prefHeightProperty().bind(scene.heightProperty());
    stage.setTitle("SVG Editor~!");
    stage.setScene(scene);
    stage.show();
  }

  private static Pane getPane() {
    var pane = new Pane();
    pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
    pane.setPrefHeight(Screen.getPrimary().getBounds().getHeight()*.8);
    pane.setPrefWidth(Screen.getPrimary().getBounds().getWidth()*.8);

    pane.setOnMousePressed(e -> {
      if(e.getButton()==MouseButton.PRIMARY){
        var circle = new Circle(e.getX(), e.getY(),5);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.DEEPSKYBLUE);

        circle.setOnMousePressed(ee -> {
          if(ee.getButton()==MouseButton.SECONDARY)
            pane.getChildren().remove(circle);
        });
        pane.getChildren().add(circle);
      }
    });
    return pane;
  }

  private HBox getHBox() {
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
