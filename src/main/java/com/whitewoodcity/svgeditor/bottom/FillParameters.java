package com.whitewoodcity.svgeditor.bottom;

import com.whitewoodcity.control.NumberField;
import com.whitewoodcity.fxgl.vectorview.JVGLayer;
import com.whitewoodcity.javafx.binding.XBindings;
import com.whitewoodcity.svgeditor.SVGEditor;
import com.whitewoodcity.svgeditor.SVGEditorHeader;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.*;

import java.util.ArrayList;
import java.util.List;

public class FillParameters extends SVGEditorHeader {
  private final ChoiceBox<String> fillType = new ChoiceBox<>();
  private final FlowPane content = new FlowPane();

  public FillParameters() {
    super();

    fillType.getItems().addAll("Color", "LinearGradient", "RadialGradient");
    fillType.setValue("Color");

    fillType.valueProperty().addListener((_, _, newV) -> {
      var node = SVGEditor.getAppCast().rightTree.currentNodeInPane();
      if (node instanceof JVGLayer layer) {
        var fill = switch (newV) {
          case "Color" -> Color.BLACK;
          case "LinearGradient" ->
            new LinearGradient(layer.getMinX(), layer.getMinY(), layer.getMaxXY().getX(), layer.getMinY(), false, CycleMethod.NO_CYCLE, new Stop(0, Color.WHITE), new Stop(1, Color.BLACK));
          case "RadialGradient" ->
            new RadialGradient(0, layer.getMaxXY().getX() - layer.getMinX(), layer.getMinX(), layer.getMinY(), layer.getMaxXY().getX() - layer.getMinX(), false, CycleMethod.NO_CYCLE, new Stop(0, Color.WHITE), new Stop(1, Color.BLACK));
          default -> Color.RED;
        };
        layer.fillProperty().unbind();
        layer.setFill(fill);
        updateNBind(layer);
      }
    });

    content.prefWrapLengthProperty().bind(this.widthProperty().map(w -> w.doubleValue() * 3 / 4));

    this.getChildren().addAll(new Label("Fill"), fillType, content);
  }

  public void updateNBind(JVGLayer layer) {
    var fill = layer.getFill();
    switch (fill) {
      case Color color -> {
        fillType.setValue("Color");
        var picker = new ColorPicker();
        picker.setValue(color);
        content.getChildren().clear();
        content.getChildren().add(picker);

        layer.fillProperty().bind(picker.valueProperty());
      }
      case LinearGradient gradient -> {
        fillType.setValue("LinearGradient");

        var startX = new NumberField(-10000, 10000);
        var startY = new NumberField(-10000, 10000);
        var endX = new NumberField(-10000, 10000);
        var endY = new NumberField(-10000, 10000);
        startX.setText("" + gradient.getStartX());
        startY.setText("" + gradient.getStartY());
        endX.setText("" + gradient.getEndX());
        endY.setText("" + gradient.getEndY());
        startX.setPrefWidth(100);
        startY.prefWidthProperty().bind(startX.prefWidthProperty());
        endX.prefWidthProperty().bind(startX.prefWidthProperty());
        endY.prefWidthProperty().bind(startX.prefWidthProperty());
        var proportional = new CheckBox();
        proportional.setSelected(gradient.isProportional());
        var cycleMethod = cycleMethodChoiceBox();
        cycleMethod.setValue(gradient.getCycleMethod());

        var addStop = new Button("+");
        var minusStop = new Button("-");

        addStop.setOnAction(_ -> addStop());
        minusStop.setOnAction(_-> minusStop());

        content.getChildren().clear();
        content.getChildren().addAll(
          new Label("Start X:"), startX,
          new Label("Y:"), startY,
          new Label("End X:"), endX,
          new Label("Y:"), endY,
          new Label("Proportional:"), proportional,
          new Label("Cycle Method:"), cycleMethod,
          new Label("Stops:"), addStop, minusStop);

        var offsets = new SimpleListProperty<Double>(FXCollections.observableArrayList());
        var colors = new SimpleListProperty<Color>(FXCollections.observableArrayList());
        for (int i = 0; i < gradient.getStops().size(); i++) {
          var stop = gradient.getStops().get(i);
          offsets.add(stop.getOffset());
          colors.add(stop.getColor());
          var offset = new NumberField(0, 1);
          offset.setPrefWidth(50);
          offset.setText(stop.getOffset() + "");
          var color = new ColorPicker(stop.getColor());
          int j = i;
          offset.valueProperty().addListener((_, _, v) -> offsets.set(j, v.doubleValue()));
          color.valueProperty().addListener((_, _, v) -> colors.set(j, v));
          content.getChildren().addAll(offset, color);
        }

        layer.fillProperty().bind(XBindings.reduce(startX.valueProperty(), startY.valueProperty(), endX.valueProperty(), endY.valueProperty(),
          proportional.selectedProperty(), cycleMethod.valueProperty(), offsets, colors,
          (sx, sy, ex, ey, p, c, os, cs) -> new LinearGradient(sx.doubleValue(), sy.doubleValue(), ex.doubleValue(), ey.doubleValue(), p, c, stops(os, cs))));
      }
      case RadialGradient gradient -> {
        //todo
//        fillType.setValue("RadialGradient");
      }
      default -> {
      }
    }
  }

  private ChoiceBox<CycleMethod> cycleMethodChoiceBox() {
    var cycleMethod = new ChoiceBox<CycleMethod>();
    cycleMethod.getItems().addAll(CycleMethod.NO_CYCLE, CycleMethod.REFLECT, CycleMethod.REPEAT);
    cycleMethod.setValue(CycleMethod.NO_CYCLE);
    return cycleMethod;
  }

  private List<Stop> stops(List<Double> offsets, List<Color> colors) {
    var list = new ArrayList<Stop>();
    for (int i = 0; i < offsets.size(); i++) {
      list.add(new Stop(offsets.get(i), colors.get(i)));
    }
    return list;
  }

  private void addStop(){
    if(SVGEditor.getAppCast().rightTree.currentNodeInPane() instanceof JVGLayer l){

      List<Stop> stops = switch (l.getFill()){
        case LinearGradient gradient -> new ArrayList<>(gradient.getStops());
        case RadialGradient gradient -> new ArrayList<>(gradient.getStops());
        default -> new ArrayList<>();
      };

      if(stops.size()<2) return;

      var offset = (stops.getLast().getOffset() + stops.get(stops.size()-2).getOffset())/2;
      stops.add(new Stop(offset,Color.BLACK));

      switch (l.getFill()){
        case LinearGradient lg -> {
          var g = new LinearGradient(lg.getStartX(), lg.getStartY(),
            lg.getEndX(), lg.getEndY(),lg.isProportional(), lg.getCycleMethod(), stops);
          l.fillProperty().unbind();
          l.setFill(g);
          updateNBind(l);
        }
        case RadialGradient rg ->{
          var g = new RadialGradient(rg.getFocusAngle(), rg.getFocusDistance(), rg.getCenterX(), rg.getCenterY(),
            rg.getRadius(), rg.isProportional(), rg.getCycleMethod(), stops);
          l.fillProperty().unbind();
          l.setFill(g);
          updateNBind(l);
        }
        default -> {}
      }
    }
  }

  private void minusStop(){
    if(SVGEditor.getAppCast().rightTree.currentNodeInPane() instanceof JVGLayer l){

      List<Stop> stops = switch (l.getFill()){
        case LinearGradient gradient -> new ArrayList<>(gradient.getStops());
        case RadialGradient gradient -> new ArrayList<>(gradient.getStops());
        default -> new ArrayList<>();
      };

      if(stops.size()<=2) return;
      stops.remove(stops.size()-2);

      switch (l.getFill()) {
        case LinearGradient lg -> {
          var g = new LinearGradient(lg.getStartX(), lg.getStartY(),
            lg.getEndX(), lg.getEndY(),lg.isProportional(), lg.getCycleMethod(), stops);
          l.fillProperty().unbind();
          l.setFill(g);
          updateNBind(l);
        }
        case RadialGradient rg -> {
          var g = new RadialGradient(rg.getFocusAngle(), rg.getFocusDistance(), rg.getCenterX(), rg.getCenterY(),
            rg.getRadius(), rg.isProportional(), rg.getCycleMethod(), stops);
          l.fillProperty().unbind();
          l.setFill(g);
          updateNBind(l);
        }
        default -> {}
      }

    }
  }
}
