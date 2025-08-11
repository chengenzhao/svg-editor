package com.whitewoodcity.control.arrows;

import module javafx.controls;

public class CrossArrows  extends Group {

  private final Polyline headA = new Polyline();
  private final Polyline headB = new Polyline();
  private final Polyline headC = new Polyline();
  private final Polyline headD = new Polyline();
  private final Circle center = new Circle();
  private final SimpleDoubleProperty x = new SimpleDoubleProperty();
  private final SimpleDoubleProperty y = new SimpleDoubleProperty();
  private final SimpleDoubleProperty x1 = new SimpleDoubleProperty();
  private final SimpleDoubleProperty y1 = new SimpleDoubleProperty();
  private final SimpleDoubleProperty x2 = new SimpleDoubleProperty();
  private final SimpleDoubleProperty y2 = new SimpleDoubleProperty();
  private final SimpleDoubleProperty x3 = new SimpleDoubleProperty();
  private final SimpleDoubleProperty y3 = new SimpleDoubleProperty();
  private final SimpleDoubleProperty x4 = new SimpleDoubleProperty();
  private final SimpleDoubleProperty y4 = new SimpleDoubleProperty();
  private final SimpleBooleanProperty headAVisible = new SimpleBooleanProperty(true);
  private final SimpleBooleanProperty headBVisible = new SimpleBooleanProperty(true);
  private final SimpleBooleanProperty headCVisible = new SimpleBooleanProperty(true);
  private final SimpleBooleanProperty headDVisible = new SimpleBooleanProperty(true);
  private final double ARROW_SCALER = 20;
  private final double ARROWHEAD_ANGLE = Math.toRadians(15);
  private final double ARROWHEAD_LENGTH = 20;

  public CrossArrows(double x, double y, double arrowDistance){
    this(x - arrowDistance,y,x + arrowDistance, y, x,y-arrowDistance, x,y+arrowDistance);
    this.x.set(x);
    this.y.set(y);
  }

  public CrossArrows(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
    this.x1.set(x1);
    this.y1.set(y1);
    this.x2.set(x2);
    this.y2.set(y2);
    this.x3.set(x3);
    this.y3.set(y3);
    this.x4.set(x4);
    this.y4.set(y4);

    getChildren().addAll(center, headA, headB, headC, headD);

    for(SimpleDoubleProperty s : new SimpleDoubleProperty[]{this.x1,this.y1,this.x2,this.y2,this.x3,this.y3,this.x4,this.y4}){
      s.addListener( (_,_,_) -> update() );
    }

    setUpStyleClassStructure();

    headA.visibleProperty().bind(headAVisible);
    headB.visibleProperty().bind(headBVisible);
    headC.visibleProperty().bind(headCVisible);
    headD.visibleProperty().bind(headDVisible);
    update();
  }

  private void setUpStyleClassStructure() {

    headA.setStroke(Color.LIMEGREEN);
    headA.setStrokeWidth(5);
    headA.setStrokeLineCap(StrokeLineCap.ROUND);
    headA.setStrokeLineJoin(StrokeLineJoin.MITER);
    headB.strokeWidthProperty().bind(headA.strokeWidthProperty());
    headB.strokeProperty().bind(headA.strokeProperty());
    headB.strokeLineCapProperty().bind(headA.strokeLineCapProperty());
    headB.strokeLineJoinProperty().bind(headA.strokeLineJoinProperty());
    headC.strokeWidthProperty().bind(headA.strokeWidthProperty());
    headC.strokeProperty().bind(headA.strokeProperty());
    headC.strokeLineCapProperty().bind(headA.strokeLineCapProperty());
    headC.strokeLineJoinProperty().bind(headA.strokeLineJoinProperty());
    headD.strokeWidthProperty().bind(headA.strokeWidthProperty());
    headD.strokeProperty().bind(headA.strokeProperty());
    headD.strokeLineCapProperty().bind(headA.strokeLineCapProperty());
    headD.strokeLineJoinProperty().bind(headA.strokeLineJoinProperty());
    center.radiusProperty().bind(headA.strokeWidthProperty());
    center.fillProperty().bind(headA.strokeProperty());
    center.centerXProperty().bindBidirectional(x);
    center.centerYProperty().bindBidirectional(y);
  }

  private void update() {
    double[] start = scale(x1.get(), y1.get(), x2.get(), y2.get());
    double[] end = scale(x2.get(), y2.get(), x1.get(), y1.get());

    double x1 = start[0];
    double y1 = start[1];
    double x2 = end[0];
    double y2 = end[1];

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

    start = scale(x3.get(), y3.get(), x4.get(), y4.get());
    end = scale(x4.get(), y4.get(), x3.get(), y3.get());

    double x3 = start[0];
    double y3 = start[1];
    double x4 = end[0];
    double y4 = end[1];

    theta = Math.atan2(y4-y3, x4-x3);

    //arrowhead 3
    x = x3 + Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    y = y3 + Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    headC.getPoints().setAll(x,y,x3,y3);
    x = x3 + Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    y = y3 + Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    headC.getPoints().addAll(x,y);
    //arrowhead 4
    x = x4 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    y = y4 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    headD.getPoints().setAll(x,y,x4,y4);
    x = x4 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    y = y4 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
    headD.getPoints().addAll(x,y);
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

  public void setX3(double x3) {
    this.x3.set(x3);
  }
  public void setX4(double x4) {
    this.x4.set(x4);
  }

  public Polyline getHeadA() {
    return headA;
  }

  public Polyline getHeadC() {
    return headC;
  }

  public Polyline getHeadD() {
    return headD;
  }

  public double getX3() {
    return x3.get();
  }

  public SimpleDoubleProperty x3Property() {
    return x3;
  }

  public double getY3() {
    return y3.get();
  }

  public SimpleDoubleProperty y3Property() {
    return y3;
  }

  public double getX4() {
    return x4.get();
  }

  public SimpleDoubleProperty x4Property() {
    return x4;
  }

  public double getY4() {
    return y4.get();
  }

  public SimpleDoubleProperty y4Property() {
    return y4;
  }

  public boolean isHeadCVisible() {
    return headCVisible.get();
  }

  public SimpleBooleanProperty headCVisibleProperty() {
    return headCVisible;
  }

  public boolean isHeadDVisible() {
    return headDVisible.get();
  }

  public SimpleBooleanProperty headDVisibleProperty() {
    return headDVisible;
  }

  public Circle getCenter() {
    return center;
  }

  public Polyline getHeadB() {
    return headB;
  }

}