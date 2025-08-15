package com.whitewoodcity.svgeditor;

import module javafx.controls;

public class SVGEditor extends Application {
  @Override
  public void start(Stage stage) {
    var pane = new Pane();

    var rect = new Rectangle(0,0,50,50);
    rect.setFill(Color.TRANSPARENT);
    rect.setStroke(Color.BLUE);
    var group = new Group(rect);

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