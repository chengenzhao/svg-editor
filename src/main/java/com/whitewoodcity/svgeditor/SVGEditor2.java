package com.whitewoodcity.svgeditor;

import module javafx.controls;
import com.whitewoodcity.svgpathcommand.*;

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

  SVGPath svgPath = new SVGPath();

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

  private Pane getPane() {
    var pane = new Pane();
    pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
    pane.setPrefHeight(Screen.getPrimary().getBounds().getHeight() * .8);
    pane.setPrefWidth(Screen.getPrimary().getBounds().getWidth() * .8);

    svgPath.setStroke(Color.BLACK);
    svgPath.setStrokeWidth(1);
    svgPath.setFill(Color.TRANSPARENT);
    pane.getChildren().add(svgPath);

    pane.setOnMousePressed(e -> {
      if (e.getButton() == MouseButton.PRIMARY) {
        var circle = new Circle(e.getX(), e.getY(), 5);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.DEEPSKYBLUE);

        circle.setOnMousePressed(ee -> {
          if (ee.getButton() == MouseButton.SECONDARY) {
            pathCommands.removeIf(c -> Math.abs(c.getX() - circle.getCenterX()) < 0.01 && Math.abs(c.getY() - circle.getCenterY())<0.01 );
            pane.getChildren().remove(circle);
            updateSVGPath();
          }
        });
        pane.getChildren().add(circle);
        if (m.isSelected()) {
          pathCommands.add(new MoveTo(new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY())));
        } else if (l.isSelected()) {
          pathCommands.add(new LineTo(new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY())));
        } else if (t.isSelected()) {
          pathCommands.add(new TransitTo(new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY())));
        } //todo the rest
        updateSVGPath();
      }
    });

    return pane;
  }

  private void updateSVGPath() {
    StringBuilder content = new StringBuilder();
    for (SVGPathCommand command : pathCommands) {
      content.append(command.command()).append(" ").append(switch (command) {
        case CurveTo curveTo -> curveTo.getX1() + "," + curveTo.y1() + " " + curveTo.x2() + "," + curveTo.y2() + " ";
        case SmoothTo smoothTo -> smoothTo.getX2() + "," + smoothTo.getY2() + " ";
        case QuadraticTo quadraticTo -> quadraticTo.getX1() + "," + quadraticTo.getY1() + " ";
        default -> "";
      }).append(command.getX()).append(",").append(command.getY()).append(" ");
    }
    svgPath.setContent(content.toString());
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
