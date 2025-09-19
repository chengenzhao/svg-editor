package com.whitewoodcity.svgeditor;

import module com.google.common;
import module com.whitewoodcity.fxcity;
import module java.base;
import module javafx.controls;
import javafx.scene.control.Label;

public class RightTree extends VBox {

  public Button addVectorButton = new Button("Vector");
  public Button addBitmapButton = new Button("Bitmap");
  public TreeView<Node> treeView = new TreeView<>(new TreeItem<>());
  private final BiMap<TreeItem<Node>, Node> itemGraphicBiMap = HashBiMap.create();

  public RightTree() {

    addVectorButton.setOnAction(_ -> {
      var item = createSVGPath();
      treeView.getSelectionModel().select(item);
    });
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
          case JVGLayer layer -> {
            var top = SVGEditor2.getAppCast().topBox;
            layer.strokeProperty().unbind();
            top.strokeParameters.getStrokeWidth().valueProperty().unbindBidirectional(layer.strokeWidthProperty());
            top.blendModeChoice.getChoiceBox().valueProperty().unbindBidirectional(layer.blendModeProperty());
            layer.fillProperty().unbind();

            switch (layer.getEffect()){
              case GaussianBlur gaussianBlur -> gaussianBlur.radiusProperty().unbindBidirectional(top.effectParameters.getZoomedFields().getFirst().valueProperty());
              case null, default -> {}
            }

            var list = layer.getSvgPathElements();
            SVGEditor2.getAppCast().cleanShapes();
          }
          default -> IO.print("???");
        }
      }
      switch (itemGraphicBiMap.get(newV)){
        case null -> {}
        case ImageView view-> {

        }
        case JVGLayer layer -> {
          var top = SVGEditor2.getAppCast().topBox;
          top.strokeParameters.getStroke().setValue((Color) (layer.getStroke()==null?Color.BLACK:layer.getStroke()));
          top.strokeParameters.getStrokeWidth().setText(layer.getStrokeWidth()+"");
          top.fillParameters.getFill().setValue((Color) layer.getFill());
          top.effectParameters.setNode(layer);

          layer.strokeProperty().bind(top.strokeParameters.getStroke().valueProperty());
          layer.fillProperty().bind(top.fillParameters.getFill().valueProperty());

          top.strokeParameters.getStrokeWidth().valueProperty().bindBidirectional(layer.strokeWidthProperty());
          top.blendModeChoice.getChoiceBox().valueProperty().bindBidirectional(layer.blendModeProperty());

          var list = layer.getSvgPathElements();
          SVGEditor2.getAppCast().buildEditableShapes(list);

          if(layer.getContent().endsWith("Z"))
            SVGEditor2.getAppCast().topBox.pathElements.getZ().setSelected(true);
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
    if(file == null) return;
    var image = new Image(file.toURI().toString());
    var imageView = new ImageView(image);
    imageView.setPreserveRatio(true);
    imageView.setFitWidth(image.getWidth());
    imageView.setFitHeight(image.getHeight());

    SVGEditor2.getAppCast().center.getChildren().addFirst(imageView);

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
    hBox.getChildren().addFirst(b);

    var del = new Button("❌");
    hBox.getChildren().add(del);

    up.setOnAction(_ -> moveUp(item));

    down.setOnAction(_ -> moveDown(item));

    del.setOnAction(_ -> del(item));

    rearrangePane();
    treeView.getSelectionModel().select(item);
  }

  public TreeItem<Node> createSVGPath(){
    return createSVGPath(null);
  }

  public TreeItem<Node> createSVGPath(TreeItem<Node> parent){
    return createSVGPath("Layer "+treeView.getRoot().getChildren().size(), parent);
  }

  public TreeItem<Node> createSVGPath(String name,TreeItem<Node> parent) {
    JVGLayer JVGLayer = new JVGLayer();

    SVGEditor2.getAppCast().center.getChildren().add(JVGLayer);

    var hBox = new HBox(new Label(name));
    hBox.setSpacing(10);
    hBox.setPadding(new Insets(5));
    hBox.setAlignment(Pos.BASELINE_LEFT);

    var item = new TreeItem<Node>(hBox);

    itemGraphicBiMap.put(item, JVGLayer);

    if(parent!=null) {
      parent.getChildren().addFirst(item);

      JVGLayer.setCache(true);
//      svgPath.setBlendMode(BlendMode.MULTIPLY);
//        n.setCacheHint(CacheHint.QUALITY);
      JVGLayer.setClip(((JVGLayer)itemGraphicBiMap.get(parent)).daemon());
    }
    else
      treeView.getRoot().getChildren().addFirst(item);

    var up = new Button("↑");
    var down = new Button("↓");
    up.setMinWidth(30);
    down.setMinWidth(30);
    var b = new HBox(up,down);
    b.setAlignment(Pos.BASELINE_LEFT);
    hBox.getChildren().addFirst(b);

    var del = new Button("❌");
    hBox.getChildren().addAll(del);
    del.setOnAction(_ -> del(item));

    if(parent==null) {
      var plus = new Button("+");
      hBox.getChildren().addAll(plus);
      plus.setOnAction(_ -> {
        var subItem = createSVGPath("SubLayer" + item.getChildren().size(),item);
        treeView.getSelectionModel().select(subItem);
      });
    }

    up.setOnAction(_ -> moveUp(item));

    down.setOnAction(_ -> moveDown(item));

    rearrangePane();

    return item;
  }

  private void rearrangePane(){
    var nodes = SVGEditor2.getAppCast().center.getChildren();
    nodes.clear();
    var list = treeView.getRoot().getChildren();
    for(var item:list){
      var l = item.getChildren();
      for(var i:l){
        nodes.addFirst(itemGraphicBiMap.get(i));
      }
      nodes.addFirst(itemGraphicBiMap.get(item));
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
    SVGEditor2.getAppCast().cleanShapes();
    if(!item.getChildren().isEmpty()){
      var l = new ArrayList<>(item.getChildren());
      l.forEach(this::del);
      item.getChildren().clear();
    }
    delRecursively(item, treeView.getRoot().getChildren());
    switch(itemGraphicBiMap.remove(item)){
      case ImageView view -> SVGEditor2.getAppCast().center.getChildren().remove(view);
      case JVGLayer layer -> SVGEditor2.getAppCast().center.getChildren().remove(layer);
      case null, default -> {}
    }
    rearrangePane();
    treeView.getSelectionModel().select(null);
  }

  private void delRecursively(TreeItem<Node> item, ObservableList<TreeItem<Node>> list){
    list.remove(item);
    for(var i:list){
      delRecursively(item, i.getChildren());
    }
  }

  public Node getNodeByItem(TreeItem<Node> item){
    return this.itemGraphicBiMap.get(item);
  }
}

class TreeItemReference{
  private TreeItem<Node> item;

  public TreeItem<Node> get() {
    return item;
  }

  public void set(TreeItem<Node> item) {
    this.item = item;
  }
}
