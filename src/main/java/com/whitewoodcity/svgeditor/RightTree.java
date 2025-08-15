package com.whitewoodcity.svgeditor;

import module javafx.controls;
import module com.google.common;
import module java.base;

import com.whitewoodcity.javafx.binding.XBindings;
import com.whitewoodcity.svgpathcommand.SVGPathElement;
import java.io.File;
import javafx.scene.control.Label;

public class RightTree extends VBox {

  public Button addVectorButton = new Button("Vector");
  public Button addBitmapButton = new Button("Bitmap");
  public TreeView<Node> treeView = new TreeView<>(new TreeItem<Node>());
  private BiMap<TreeItem, Node> itemGraphicBiMap = HashBiMap.create();
  private Map<SVGPath, List<SVGPathElement>> svgPathListMap = new HashMap<>();
  private Map<SVGPathElement, List<Shape>> commandCircleMap = new HashMap<>();

  public RightTree() {

    addVectorButton.setOnAction(_ -> createSVGPath());
    addBitmapButton.setOnAction(_ -> createImageView());

    var buttonBox = new HBox(new Label("+"), addVectorButton, addBitmapButton);
    buttonBox.setAlignment(Pos.BASELINE_LEFT);
    buttonBox.setSpacing(10);
    buttonBox.setPadding(new Insets(10));
    treeView.setShowRoot(false);
    treeView.prefHeightProperty().bind(XBindings.reduceDoubleValue(this.heightProperty(), buttonBox.heightProperty(), (vh, rh) -> vh - rh));
    this.getChildren().addAll(buttonBox, treeView);

    treeView.getSelectionModel().selectedItemProperty().addListener((_,oldV,newV)->{
      if(oldV!=null) {
        switch (itemGraphicBiMap.get(oldV)) {
          case null -> {}
          case ImageView view -> {

          }
          case SVGPath svgPath -> {
            svgPath.strokeProperty().unbind();
            svgPath.strokeWidthProperty().unbind();
            svgPath.fillProperty().unbind();

            var list = svgPathListMap.get(svgPath);
            list.forEach(e -> commandCircleMap.get(e).forEach(s -> SVGEditor2.getAppCast().center.getChildren().remove(s)));
          }
          default -> IO.print("???");
        }
      }
      switch (itemGraphicBiMap.get(newV)){
        case null -> {}
        case ImageView view-> {

        }
        case SVGPath svgPath -> {
          var top = SVGEditor2.getAppCast().topBox;
          top.strokeParameters.getStroke().setValue((Color) (svgPath.getStroke()==null?Color.BLACK:svgPath.getStroke()));
          top.strokeParameters.getStrokeWidth().setText(svgPath.getStrokeWidth()+"");
          top.fillParameters.getFill().setValue((Color) svgPath.getFill());
          top.effectParameters.setNode(svgPath);
          svgPath.strokeProperty().bind(top.strokeParameters.getStroke().valueProperty());
          svgPath.strokeWidthProperty().bind(top.strokeParameters.getStrokeWidth().textProperty().map(t -> Double.parseDouble(t.toString())));
          svgPath.fillProperty().bind(top.fillParameters.getFill().valueProperty());

          var list = svgPathListMap.get(svgPath);
          list.forEach(e -> commandCircleMap.get(e).forEach(s -> SVGEditor2.getAppCast().center.getChildren().add(s)));
        }
        default -> IO.print("???");
      }
    });
  }

  public Node currentNodeInPane(){
    return itemGraphicBiMap.get(treeView.getSelectionModel().getSelectedItem());
  }

  public void createImageView() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", "*.PNG", "*.JPG"));
    File file = fileChooser.showOpenDialog(this.getScene().getWindow());
    var image = new Image(file.toURI().toString());
    var imageView = new ImageView(image);
    imageView.setPreserveRatio(true);
    imageView.setFitWidth(image.getWidth());
    imageView.setFitHeight(image.getHeight());

    SVGEditor2.getAppCast().center.getChildren().add(0, imageView);

    var hBox = new HBox(new Label(file.getName()));
    hBox.setSpacing(10);
    hBox.setPadding(new Insets(5));
    hBox.setAlignment(Pos.BASELINE_LEFT);

    var item = new TreeItem<Node>(hBox);

    itemGraphicBiMap.put(item, imageView);

    treeView.getRoot().getChildren().add(item);

    var up = new Button("↑");
    var down = new Button("↓");
    up.setMinWidth(30);
    down.setMinWidth(30);
    var b = new HBox(up,down);
    b.setAlignment(Pos.BASELINE_LEFT);
    hBox.getChildren().add(0, b);

    var del = new Button("❌");
    hBox.getChildren().add(del);

    up.setOnAction(_ -> moveUp(item));

    down.setOnAction(_ -> moveDown(item));

    del.setOnAction(_ -> del(item));

    rearrangePane();
    treeView.getSelectionModel().select(item);
  }

  public TreeItem<Node> createSVGPath(){
    return createSVGPath("Layer "+treeView.getRoot().getChildren().size(), null);
  }

  public TreeItem<Node> createSVGPath(String name,TreeItem<Node> parent) {
    SVGPath svgPath = new SVGPath();

    SVGEditor2.getAppCast().center.getChildren().add(svgPath);

    var hBox = new HBox(new Label(name));
    hBox.setSpacing(10);
    hBox.setPadding(new Insets(5));
    hBox.setAlignment(Pos.BASELINE_LEFT);

    var item = new TreeItem<Node>(hBox);

    itemGraphicBiMap.put(item, svgPath);
    svgPathListMap.put(svgPath, new ArrayList<SVGPathElement>());

    if(parent!=null) {
      parent.getChildren().add(0, item);

      svgPath.setCache(true);
//      svgPath.setBlendMode(BlendMode.MULTIPLY);
//        n.setCacheHint(CacheHint.QUALITY);
      svgPath.setClip(clone((SVGPath) itemGraphicBiMap.get(parent)));
    }
    else
      treeView.getRoot().getChildren().add(0,item);

    var up = new Button("↑");
    var down = new Button("↓");
    up.setMinWidth(30);
    down.setMinWidth(30);
    var b = new HBox(up,down);
    b.setAlignment(Pos.BASELINE_LEFT);
    hBox.getChildren().add(0, b);

    var del = new Button("❌");
    hBox.getChildren().addAll(del);
    del.setOnAction(_ -> del(item));

    if(parent==null) {
      var plus = new Button("+");
      hBox.getChildren().addAll(plus);
      plus.setOnAction(_ -> createSVGPath("SubLayer" + item.getChildren().size(),item));
    }

    up.setOnAction(_ -> moveUp(item));

    down.setOnAction(_ -> moveDown(item));

    rearrangePane();
    treeView.getSelectionModel().select(item);

    return item;
  }

  private void rearrangePane(){
    var nodes = SVGEditor2.getAppCast().center.getChildren();
    nodes.clear();
    var list = treeView.getRoot().getChildren();
    for(var item:list){
      var l = item.getChildren();
      for(var i:l){
        nodes.add(0, itemGraphicBiMap.get(i));
      }
      nodes.add(0,itemGraphicBiMap.get(item));
    }
  }

  private void moveUp(TreeItem<Node> item){
    var children = item.getParent().getChildren();
    var index = children.indexOf(item);
    if (index > 0) {
      children.add(index - 1, children.remove(index));
    }
    rearrangePane();
    treeView.getSelectionModel().select(item);
  }

  private void moveDown(TreeItem<Node> item){
    var children = item.getParent().getChildren();
    var index = children.indexOf(item);
    if (index >= 0 && index < children.size() - 1) {
      children.add(index + 1, children.remove(index));
    }
    rearrangePane();
    treeView.getSelectionModel().select(item);
  }

  private void del(TreeItem<Node> item){
    if(item.getChildren().size()>0){
      item.getChildren().forEach(c -> del(c));
    }
    delRecursively(item, treeView.getRoot().getChildren());
    switch(itemGraphicBiMap.remove(item)){
      case null -> {}
      case ImageView view -> SVGEditor2.getAppCast().center.getChildren().remove(view);
      case SVGPath path -> {
        SVGEditor2.getAppCast().center.getChildren().remove(path);
        svgPathListMap.get(path).forEach(e -> commandCircleMap.get(e).forEach(s -> SVGEditor2.getAppCast().center.getChildren().remove(s)));
      }
      default -> {}
    }
    rearrangePane();
  }

  private void delRecursively(TreeItem<Node> item, ObservableList<TreeItem<Node>> list){
    if(list.contains(item)) list.remove(item);
    for(var i:list){
      delRecursively(item, i.getChildren());
    }
  }

  public List<SVGPathElement> getSVGPathElements(SVGPath svgPath){
    return svgPathListMap.get(svgPath);
  }

  public Map<SVGPathElement, List<Shape>> getCommandCircleMap() {
    return commandCircleMap;
  }

  public SVGPath clone(SVGPath path){
    var svgPath = new SVGPath();
    svgPath.contentProperty().bind(path.contentProperty());
    svgPath.strokeProperty().bind(path.strokeProperty());
    svgPath.strokeWidthProperty().bind(path.strokeWidthProperty());
    svgPath.strokeLineJoinProperty().bind(path.strokeLineJoinProperty());
    svgPath.strokeLineCapProperty().bind(path.strokeLineCapProperty());
    svgPath.effectProperty().bind(path.effectProperty());
    return svgPath;
  }
}
