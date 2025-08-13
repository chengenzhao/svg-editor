package com.whitewoodcity.svgeditor;

import module javafx.controls;
import module java.base;

import com.whitewoodcity.svgpathcommand.*;

public class SVGEditor2 extends Application {
  PathElements pathElements = new PathElements();
  StrokeParameters strokeParameters = new StrokeParameters();
  FillParameters fillParameters = new FillParameters();

  List<SVGPathElement> svgPathElements = new ArrayList<>();

  SVGPath svgPath = new SVGPath();

  @Override
  public void start(Stage stage) {
    var topBox = new VBox();
    pathElements.getZ().selectedProperty().addListener((_, _, _) -> updateSVGPath());
    topBox.getChildren().addAll(pathElements, strokeParameters, fillParameters);

    var borderPane = new BorderPane();

    var left = new LeftColumn();

    left.getZoomIn().setOnAction(_ -> updateSVGPathElements(svgPathElements, v -> v.doubleValue() * left.getFactor()));
    left.getZoomOut().setOnAction(_ -> updateSVGPathElements(svgPathElements, v -> v.doubleValue() / left.getFactor()));

    borderPane.setTop(topBox);
    borderPane.setCenter(getPane());
    borderPane.setLeft(left);

    Scene scene = new Scene(borderPane);
    stage.setTitle("SVG Path Editor~!");
    stage.setScene(scene);
    stage.show();
  }

  private Map<SVGPathElement, List<Shape>> commandCircleMap = new HashMap<>();

  private void addShapeToMap(SVGPathElement command, Shape shape) {
    var list = commandCircleMap.get(command);
    if (list == null) {
      list = new ArrayList<>();
      commandCircleMap.put(command, list);
    }
    list.add(shape);
  }

  private void removeSVGPathCommand(Pane pane, SVGPathElement command) {
    if(commandCircleMap.size() > 1 && svgPathElements.indexOf(command) == 0){
      return;
    }
    var list = commandCircleMap.remove(command);
    if (list != null) {
      pane.getChildren().removeAll(list);
    }
    svgPathElements.remove(command);
  }

  private Line makeLine(Circle c0, Circle c1) {
    var line = new Line();
    line.startXProperty().bindBidirectional(c0.centerXProperty());
    line.endXProperty().bindBidirectional(c1.centerXProperty());
    line.startYProperty().bindBidirectional(c0.centerYProperty());
    line.endYProperty().bindBidirectional(c1.centerYProperty());
    line.setStroke(Color.RED);
    line.getStrokeDashArray().addAll(5.0, 5.0);
    return line;
  }

  private Circle makeCircle(SimpleDoubleProperty x, SimpleDoubleProperty y) {
    return makeCircle(x, y, null);
  }

  private Circle makeCircle(SimpleDoubleProperty x, SimpleDoubleProperty y, Runnable deletable) {
    var circle = new Circle(x.get(), y.get(), 5);
    circle.setFill(Color.TRANSPARENT);
    if (deletable == null)
      circle.setStroke(Color.RED);
    else
      circle.setStroke(Color.DEEPSKYBLUE);

    circle.setOnMousePressed(e -> {
      if (e.getButton() == MouseButton.SECONDARY) {
        if (deletable != null) {
          deletable.run();
          updateSVGPath();
        }
      } else {
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
      }
      e.consume();
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

    svgPath.strokeProperty().bind(strokeParameters.getStroke().valueProperty());
    svgPath.strokeWidthProperty().bind(strokeParameters.getStrokeWidth().textProperty().map(t -> Double.parseDouble(t.toString())));
    svgPath.fillProperty().bind(fillParameters.getFill().valueProperty());
    pane.getChildren().add(svgPath);

    pane.setOnMousePressed(e -> {
      if (e.getButton() == MouseButton.PRIMARY) {
        var previousCommand = svgPathElements.size() > 0 ? svgPathElements.getLast() : null;
        SVGPathElement command = (svgPathElements.size() < 1 || pathElements.getM().isSelected()) ? new MoveTo(new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY())) :
          pathElements.getL().isSelected() ? new LineTo(new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY())) :
            pathElements.getT().isSelected() ? new TransitTo(new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY())) :
              pathElements.getC().isSelected() ? new CurveTo(new SimpleDoubleProperty(previousCommand.getX() + 50), new SimpleDoubleProperty(previousCommand.getY()), new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY() - 50), new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY())) :
                pathElements.getS().isSelected() ? new SmoothTo(new SimpleDoubleProperty(e.getX() - 50), new SimpleDoubleProperty(e.getY()), new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY())) :
                  new QuadraticTo(new SimpleDoubleProperty((previousCommand.getX() + e.getX()) / 2), new SimpleDoubleProperty((previousCommand.getY() + e.getY()) / 2), new SimpleDoubleProperty(e.getX()), new SimpleDoubleProperty(e.getY()));

        switch (command) {
          case CurveTo c -> {
            var circle = makeCircle(command.x(), command.y(), () -> removeSVGPathCommand(pane, command));
            var c1 = makeCircle(c.x1(), c.y1());
            var c2 = makeCircle(c.x2(), c.y2());
            var l = makeLine(c2, circle);

            addShapeToMap(command, circle);
            addShapeToMap(command, c1);
            addShapeToMap(command, c2);
            addShapeToMap(command, l);
            pane.getChildren().addAll(circle, c1, c2, l);
          }
          case QuadraticTo q -> {
            var circle = makeCircle(command.x(), command.y(), () -> removeSVGPathCommand(pane, command));
            var c1 = makeCircle(q.x1(), q.y1());
            var l = makeLine(c1, circle);

            addShapeToMap(command, circle);
            addShapeToMap(command, c1);
            addShapeToMap(command, l);
            pane.getChildren().addAll(circle, c1, l);
          }
          case SmoothTo s -> {
            var circle = makeCircle(command.x(), command.y(), () -> removeSVGPathCommand(pane, command));
            var c2 = makeCircle(s.x2(), s.y2());
            var l = makeLine(c2, circle);

            addShapeToMap(command, circle);
            addShapeToMap(command, c2);
            addShapeToMap(command, l);
            pane.getChildren().addAll(circle, c2, l);
          }
          default -> {
            var circle = makeCircle(command.x(), command.y(), () -> removeSVGPathCommand(pane, command));
            addShapeToMap(command, circle);
            pane.getChildren().add(circle);
          }
        }
        svgPathElements.add(command);

        updateSVGPath();

      } else {

        var ox = e.getX();
        var oy = e.getY();

        var oes = new ArrayList<SVGPathElement>();

        for (var element : svgPathElements) {
          oes.add(element.clone());
        }

        pane.setOnMouseDragged(event -> {

          var dx = event.getX() - ox;
          var dy = event.getY() - oy;

          updateSVGPathElements(oes,x -> x.doubleValue() + dx, y -> y.doubleValue() + dy);

          event.consume();
        });

        pane.setOnMouseReleased(_ -> pane.setOnMouseDragged(_ -> {}));
      }
    });

    return pane;
  }

  private void updateSVGPath() {
    StringBuilder content = new StringBuilder();
    for (SVGPathElement element : svgPathElements) {
      content.append(element.command()).append(" ").append(switch (element) {
        case CurveTo curveTo ->
          curveTo.getX1() + "," + curveTo.getY1() + " " + curveTo.getX2() + "," + curveTo.getY2() + " ";
        case SmoothTo smoothTo -> smoothTo.getX2() + "," + smoothTo.getY2() + " ";
        case QuadraticTo quadraticTo -> quadraticTo.getX1() + "," + quadraticTo.getY1() + " ";
        default -> "";
      }).append(element.getX()).append(",").append(element.getY()).append(" ");
    }
    if (pathElements.getZ().isSelected()) content.append("Z");
    svgPath.setContent(content.toString());
  }

  private void updateSVGPathElements(List<SVGPathElement> references, SVGPathElement.Apply apply){
    updateSVGPathElements(references, apply, apply);
  }
  private void updateSVGPathElements(List<SVGPathElement> references, SVGPathElement.Apply applyX, SVGPathElement.Apply applyY){
    for (int i = 0; i < svgPathElements.size(); i++) {
      var re = references.get(i);
      var e = svgPathElements.get(i);

      e.apply(re, applyX, applyY);
    }
    updateSVGPath();
  }

}
