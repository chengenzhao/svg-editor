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

  List<SVGPathElement> svgPathElements = new ArrayList<>();

  SVGPath svgPath = new SVGPath();

  @Override
  public void start(Stage stage) {
    var vBox = new VBox();

    var pathElements = getHBox();

    var pane = getPane();

    vBox.getChildren().addAll(pathElements, pane);
    vBox.setPrefWidth(pane.getPrefWidth());
    vBox.setPrefHeight(pane.getPrefHeight() + pathElements.getHeight());

    Scene scene = new Scene(vBox, vBox.getPrefWidth(), vBox.getPrefHeight());
    vBox.prefWidthProperty().bind(scene.widthProperty());
    vBox.prefHeightProperty().bind(scene.heightProperty());
    stage.setTitle("SVG Path Editor~!");
    stage.setScene(scene);
    stage.show();
  }

  private Map<SVGPathElement, List<Shape>> commandCircleMap = new HashMap<>();

  private void addShapeToMap(SVGPathElement command, Shape shape){
    var list = commandCircleMap.get(command);
    if(list == null) {
      list = new ArrayList<>();
      commandCircleMap.put(command, list);
    }
    list.add(shape);
  }

  private void removeSVGPathCommand(Pane pane, SVGPathElement command){
    var list = commandCircleMap.remove(command);
    if(list!=null){
      pane.getChildren().removeAll(list);
    }
    svgPathElements.remove(command);
  }

  private Line makeLine(Circle c0, Circle c1){
    var line = new Line();
    line.startXProperty().bindBidirectional(c0.centerXProperty());
    line.endXProperty().bindBidirectional(c1.centerXProperty());
    line.startYProperty().bindBidirectional(c0.centerYProperty());
    line.endYProperty().bindBidirectional(c1.centerYProperty());
    line.setStroke(Color.RED);
    line.getStrokeDashArray().addAll(5.0,5.0);
    return line;
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
        var previousCommand = svgPathElements.size() > 0 ? svgPathElements.getLast():null;
        SVGPathElement command = (svgPathElements.size() < 1 || m.isSelected()) ? new MoveTo(new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY())) :
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
            var l = makeLine(c2,circle);

            addShapeToMap(command, circle);
            addShapeToMap(command, c1);
            addShapeToMap(command, c2);
            addShapeToMap(command, l);
            pane.getChildren().addAll(circle,c1,c2,l);
          }
          case QuadraticTo q ->{
            var circle = makeCircle(command.x(), command.y(), () -> removeSVGPathCommand(pane, command));
            var c1 = makeCircle(q.x1(), q.y1());
            var l = makeLine(c1,circle);

            addShapeToMap(command, circle);
            addShapeToMap(command, c1);
            addShapeToMap(command, l);
            pane.getChildren().addAll(circle,c1,l);
          }
          case SmoothTo s -> {
            var circle = makeCircle(command.x(), command.y(), () -> removeSVGPathCommand(pane, command));
            var c2 = makeCircle(s.x2(), s.y2());
            var l = makeLine(c2, circle);

            addShapeToMap(command, circle);
            addShapeToMap(command, c2);
            addShapeToMap(command, l);
            pane.getChildren().addAll(circle,c2,l);
          }
          default -> {
            var circle = makeCircle(command.x(), command.y(), () -> removeSVGPathCommand(pane, command));
            addShapeToMap(command, circle);
            pane.getChildren().add(circle);
          }
        };
        svgPathElements.add(command);

        updateSVGPath();
      }
    });

    return pane;
  }

  private void updateSVGPath() {
    StringBuilder content = new StringBuilder();
    for (SVGPathElement command : svgPathElements) {
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

    z.selectedProperty().addListener((_,_,_) -> updateSVGPath());

    return hbox;
  }
}
