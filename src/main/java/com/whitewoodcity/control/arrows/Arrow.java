package com.whitewoodcity.control.arrows;

import module javafx.controls;

public class Arrow extends Group {

  private final Polyline mainLine = new Polyline();
  private final Polyline headA = new Polyline();
  private final Polyline headB = new Polyline();
  private final Circle origin = new Circle();
  private final SimpleDoubleProperty x1 = new SimpleDoubleProperty();
  private final SimpleDoubleProperty y1 = new SimpleDoubleProperty();
  private final SimpleDoubleProperty x2 = new SimpleDoubleProperty();
  private final SimpleDoubleProperty y2 = new SimpleDoubleProperty();
  private final SimpleBooleanProperty headAVisible = new SimpleBooleanProperty(false);
  private final SimpleBooleanProperty headBVisible = new SimpleBooleanProperty(true);
  private final double ARROW_SCALER = 20;
  private final double ARROWHEAD_ANGLE = Math.toRadians(20);
  private final double ARROWHEAD_LENGTH = 20;

  public Arrow(double x1, double y1, double x2, double y2){
    this.x1.set(x1);
    this.y1.set(y1);
    this.x2.set(x2);
    this.y2.set(y2);

    getChildren().addAll(origin, mainLine, headA, headB);

    for(SimpleDoubleProperty s : new SimpleDoubleProperty[]{this.x1,this.y1,this.x2,this.y2}){
      s.addListener( (_,_,_) -> update() );
    }

    setUpStyleClassStructure();

    headA.visibleProperty().bind(headAVisible);
    headB.visibleProperty().bind(headBVisible);
    update();
  }

  private void setUpStyleClassStructure() {
//    mainLine.getStyleClass().setAll("arrow");
//    headA.getStyleClass().setAll("arrow");
//    headB.getStyleClass().setAll("arrow");
//
//    headA.getStyleClass().add("arrowhead");
//    headB.getStyleClass().add("arrowhead");

//    getStyleClass().addListener( (ListChangeListener<? super String>) c -> {
//      c.next();
//      for(Polyline p : new Polyline[]{mainLine, headA, headB}){
//        p.getStyleClass().addAll(c.getAddedSubList());
//        p.getStyleClass().removeAll(c.getRemoved());
//      }
//    });

    mainLine.setStroke(Color.LIMEGREEN);
    mainLine.setStrokeWidth(5);
    mainLine.setStrokeLineCap(StrokeLineCap.ROUND);
    mainLine.setStrokeLineJoin(StrokeLineJoin.MITER);
    headB.strokeWidthProperty().bind(mainLine.strokeWidthProperty());
    headB.strokeProperty().bind(mainLine.strokeProperty());
    headB.strokeLineCapProperty().bind(mainLine.strokeLineCapProperty());
    headB.strokeLineJoinProperty().bind(mainLine.strokeLineJoinProperty());
    origin.radiusProperty().bind(mainLine.strokeWidthProperty());
    origin.fillProperty().bind(mainLine.strokeProperty());
    origin.centerXProperty().bindBidirectional(x1);
    origin.centerYProperty().bindBidirectional(y1);
  }

  private void update() {
    double[] start = scale(x1.get(), y1.get(), x2.get(), y2.get());
    double[] end = scale(x2.get(), y2.get(), x1.get(), y1.get());

    double x1 = start[0];
    double y1 = start[1];
    double x2 = end[0];
    double y2 = end[1];

    mainLine.getPoints().setAll(x1,y1,x2,y2);

    double theta = Math.atan2(y2-y1, x2-x1);

    //arrowhead 1
    double x = x1 + Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    double y = y1 + Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    headA.getPoints().setAll(x,y,x1,y1);
    x = x1 + Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    y = y1 + Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    headA.getPoints().addAll(x,y);
    //arrowhead 2
    x = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    y = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    headB.getPoints().setAll(x,y,x2,y2);
    x = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    y = y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    headB.getPoints().addAll(x,y);
  }

  private double[] scale(double x1, double y1, double x2, double y2){
    double theta = Math.atan2(y2-y1, x2-x1);
    return new double[]{
      x1 + Math.cos(theta) * ARROW_SCALER,
      y1 + Math.sin(theta) * ARROW_SCALER
    };
  }

  //getters and setters
  public double getX1() {
    return x1.get();
  }
  public SimpleDoubleProperty x1Property() {
    return x1;
  }
  public void setX1(double x1) {
    this.x1.set(x1);
  }
  public double getY1() {
    return y1.get();
  }
  public SimpleDoubleProperty y1Property() {
    return y1;
  }
  public void setY1(double y1) {
    this.y1.set(y1);
  }
  public double getX2() {
    return x2.get();
  }
  public SimpleDoubleProperty x2Property() {
    return x2;
  }
  public void setX2(double x2) {
    this.x2.set(x2);
  }
  public double getY2() {
    return y2.get();
  }
  public SimpleDoubleProperty y2Property() {
    return y2;
  }
  public void setY2(double y2) {
    this.y2.set(y2);
  }
  public boolean isHeadAVisible() {
    return headAVisible.get();
  }
  public SimpleBooleanProperty headAVisibleProperty() {
    return headAVisible;
  }
  public void setHeadAVisible(boolean headAVisible) {
    this.headAVisible.set(headAVisible);
  }
  public boolean isHeadBVisible() {
    return headBVisible.get();
  }
  public SimpleBooleanProperty headBVisibleProperty() {
    return headBVisible;
  }
  public void setHeadBVisible(boolean headBVisible) {
    this.headBVisible.set(headBVisible);
  }

  public Circle getOrigin() {
    return origin;
  }

  public Polyline getHeadB() {
    return headB;
  }

  public Polyline getMainLine() {
    return mainLine;
  }
}