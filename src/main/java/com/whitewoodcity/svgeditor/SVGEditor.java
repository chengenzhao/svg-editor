package com.whitewoodcity.svgeditor;

import module javafx.controls;

public class SVGEditor extends Application {
  @Override
  public void start(Stage stage) {
    var pane = new Group();
    var region = new Region();
    var rect = new Line(0, 0, 100, 100);
//    rect.setArcHeight(50);
//    rect.setArcWidth(50);
    region.setShape(rect);
    setFill(region, Color.PINK, Color.BLACK);
    region.setPrefWidth(50);
    region.setPrefHeight(50);
    var svgPath = new SVGPath();
    svgPath.setContent("M 0,100 L 100,0 100,100 Z");
    svgPath.setStroke(Color.BLUE);
    svgPath.setFill(Color.PINK);
    svgPath.setStrokeWidth(10);
    svgPath.setStrokeLineJoin(StrokeLineJoin.ROUND);
    svgPath.setStrokeLineCap(StrokeLineCap.ROUND);
//    var effect = new GaussianBlur();
//    svgPath.setEffect(effect);

    var line = new Line(50,0,50,100);
    line.setStrokeWidth(10);
    line.setStroke(Color.BLUE);
    line.setBlendMode(BlendMode.COLOR_DODGE);
    line.setCache(true);
    line.setCacheHint(CacheHint.DEFAULT);
    line.setClip(clone(svgPath));

    var l = new Line(20,0,20,100);
    l.setStrokeWidth(5);
    l.setStroke(Color.GREEN);
    l.setBlendMode(BlendMode.MULTIPLY);

    pane.getChildren().addAll(region,svgPath,line,l);

    Scene scene = new Scene(pane);
    stage.setTitle("Hello!");
    stage.setScene(scene);
    stage.show();
  }

  public void setFill(Region region, Paint fill, Paint stroke) {
    region.setBackground(new Background(new BackgroundFill(fill, CornerRadii.EMPTY, Insets.EMPTY)));
    region.setBorder(new Border(new BorderStroke(stroke, BorderStrokeStyle.SOLID,CornerRadii.EMPTY,new BorderWidths(10))));
  }

  public SVGPath clone(SVGPath path){
    var svgPath = new SVGPath();
    svgPath.setContent(path.getContent());
    svgPath.setStroke(path.getStroke());
    svgPath.setStrokeWidth(path.getStrokeWidth());
    svgPath.setStrokeLineJoin(path.getStrokeLineJoin());
    svgPath.setStrokeLineCap(path.getStrokeLineCap());
//    svgPath.setFill(path.getFill());
    svgPath.setEffect(path.getEffect());
    return svgPath;
  }
}