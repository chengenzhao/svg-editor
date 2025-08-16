package com.whitewoodcity.svgeditor;

import module javafx.controls;

public class SVGEditor extends Application {
  @Override
  public void start(Stage stage) {
    var pane = new Pane();

    var rect = new Rectangle(0,0,50,50);
    rect.setFill(Color.TRANSPARENT);
    rect.setStroke(Color.BLUE);

    var svgpath = new SVGPath();
    svgpath.setContent("M 0,0 T 40,0 40,40 0,40 Z");

    var group = new Group(svgpath);

    var r = new Rotate(0,20,20);

    group.getTransforms().add(r);

    KeyValue keyValue = new KeyValue(r.angleProperty(), 360);
    KeyFrame keyFrame = new KeyFrame(Duration.seconds(3), keyValue);
    Timeline timeline = new Timeline(keyFrame);
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();

    var cross = new Circle(10);
    cross.setStroke(Color.RED);
    cross.centerXProperty().bindBidirectional(group.layoutXProperty());
    cross.centerYProperty().bindBidirectional(group.layoutYProperty());

    cross.setOnMousePressed(e -> {
      var ox = e.getX();
      var oy = e.getY();
      var rx = cross.getCenterX();
      var ry = cross.getCenterY();

      cross.setOnMouseDragged(ee -> {
        var dx = ee.getX() - ox;
        var dy = ee.getY() - oy;

        cross.setCenterX(rx + dx);
        cross.setCenterY(ry + dy);
      });
    });

    pane.getChildren().addAll(group,cross);

    Scene scene = new Scene(pane);
    stage.setTitle("Hello!");
    stage.setScene(scene);
    stage.show();
  }
}