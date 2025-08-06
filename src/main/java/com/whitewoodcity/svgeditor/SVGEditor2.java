package com.whitewoodcity.svgeditor;

import module javafx.controls;
import com.whitewoodcity.svgpathcommand.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SVGEditor2 extends Application {

  RadioButton m = new RadioButton("M");
  RadioButton l = new RadioButton("L");
  RadioButton t = new RadioButton("T");
  RadioButton s = new RadioButton("S");
  RadioButton q = new RadioButton("Q");
  RadioButton c = new RadioButton("C");
  RadioButton z = new RadioButton("Z");

  List<SVGPathCommand> svgPathCommands = new ArrayList<>();

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

  private Map<SVGPathCommand, List<Circle>> commandCircleMap = new HashMap<>();

  private void addCircleToMap(SVGPathCommand command, Circle circle){
    var list = commandCircleMap.get(command);
    if(list == null) {
      list = new ArrayList<>();
      commandCircleMap.put(command, list);
    }
    list.add(circle);
  }

  private void removeSVGPathCommand(Pane pane, SVGPathCommand command){
    var list = commandCircleMap.remove(command);
    if(list!=null){
      pane.getChildren().removeAll(list);
    }
    svgPathCommands.remove(command);
  }

  private Circle makeCircle(SimpleDoubleProperty x, SimpleDoubleProperty y){
    return makeCircle(x,y,null);
  }
  private Circle makeCircle(SimpleDoubleProperty x, SimpleDoubleProperty y, Runnable deletable){
    var circle = new Circle(x.get(), y.get(), 5);
    circle.setFill(Color.TRANSPARENT);
    if(deletable == null)
      circle.setStroke(Color.RED);
    else
      circle.setStroke(Color.DEEPSKYBLUE);

    circle.setOnMousePressed(e -> {
      if (e.getButton() == MouseButton.SECONDARY) {
        if(deletable!=null){
          deletable.run();
          updateSVGPath();
        }
      }else{
        double ox = e.getX();
        double oy = e.getY();
        double cx = circle.getCenterX();
        double cy = circle.getCenterY();
        circle.setOnMouseDragged(event -> {
          var dx = event.getX() - ox;
          var dy = event.getY() - oy;
          circle.setCenterX(cx + dx);
          circle.setCenterY(cy + dy);
          updateSVGPath();
        });
        e.consume();
      }
    });

    circle.centerXProperty().bindBidirectional(x);
    circle.centerYProperty().bindBidirectional(y);

    return circle;
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
        var previousCommand = svgPathCommands.size() > 0 ? svgPathCommands.getLast():null;
        SVGPathCommand command = (svgPathCommands.size() < 1 || m.isSelected()) ? new MoveTo(new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY())) :
        l.isSelected() ? new LineTo(new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY())) :
        t.isSelected() ? new TransitTo(new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY())):
        c.isSelected() ? new CurveTo(new SimpleDoubleProperty(previousCommand.getX() + 50), new SimpleDoubleProperty(previousCommand.getY()), new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY() - 50), new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY())):
        s.isSelected() ? new SmoothTo(new SimpleDoubleProperty(e.getX() - 50), new SimpleDoubleProperty(e.getY()),new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY())):
        new QuadraticTo(new SimpleDoubleProperty((previousCommand.getX() + e.getX())/2), new SimpleDoubleProperty((previousCommand.getY()+e.getY())/2),new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY()));

        switch (command){
          case CurveTo c -> {
            var circle = makeCircle(command.x(), command.y(), () -> removeSVGPathCommand(pane, command));
            var c1 = makeCircle(c.x1(), c.y1());
            var c2 = makeCircle(c.x2(), c.y2());

            addCircleToMap(command, circle);
            addCircleToMap(command, c1);
            addCircleToMap(command, c2);
            pane.getChildren().addAll(circle,c1,c2);
          }
          case QuadraticTo q ->{
            var circle = makeCircle(command.x(), command.y(), () -> removeSVGPathCommand(pane, command));
            var c1 = makeCircle(q.x1(), q.y1());

            addCircleToMap(command, circle);
            addCircleToMap(command, c1);
            pane.getChildren().addAll(circle,c1);
          }
          case SmoothTo s -> {
            var circle = makeCircle(command.x(), command.y(), () -> removeSVGPathCommand(pane, command));
            var c2 = makeCircle(s.x2(), s.y2());

            addCircleToMap(command, circle);
            addCircleToMap(command, c2);
            pane.getChildren().addAll(circle,c2);
          }
          default -> {
            var circle = makeCircle(command.x(), command.y(), () -> removeSVGPathCommand(pane, command));
            addCircleToMap(command, circle);
            pane.getChildren().add(circle);
          }
        };
        svgPathCommands.add(command);

        updateSVGPath();
      }
    });

    return pane;
  }

  private void updateSVGPath() {
    StringBuilder content = new StringBuilder();
    for (SVGPathCommand command : svgPathCommands) {
      content.append(command.command()).append(" ").append(switch (command) {
        case CurveTo curveTo -> curveTo.getX1() + "," + curveTo.getY1() + " " + curveTo.getX2() + "," + curveTo.getY2() + " ";
        case SmoothTo smoothTo -> smoothTo.getX2() + "," + smoothTo.getY2() + " ";
        case QuadraticTo quadraticTo -> quadraticTo.getX1() + "," + quadraticTo.getY1() + " ";
        default -> "";
      }).append(command.getX()).append(",").append(command.getY()).append(" ");
    }
    if(z.isSelected()) content.append("Z");
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
    z.setToggleGroup(radioGroup);

    var hbox = new HBox(m, l, t, s, q, c,z);

    hbox.setSpacing(3);
    hbox.setPadding(new Insets(10));

    m.setSelected(true);

    z.selectedProperty().addListener((_,_,v) -> {
      updateSVGPath();
    });

    return hbox;
  }
}
