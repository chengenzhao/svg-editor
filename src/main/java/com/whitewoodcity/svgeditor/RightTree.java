package com.whitewoodcity.svgeditor;

import module javafx.controls;
import module com.google.common;
import module java.base;

import com.whitewoodcity.javafx.binding.XBindings;
import com.whitewoodcity.svgpathcommand.SVGPathElement;
import java.io.File;
import javafx.scene.control.Label;
import com.whitewoodcity.svgpathcommand.SVGPathElement;

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
          top.strokeParameters.getStrokeWidth().setPrefWidth(svgPath.getStrokeWidth());
          top.fillParameters.getFill().setValue((Color) svgPath.getFill());
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

    SVGEditor2.getAppCast().center.getChildren().add(0, imageView);

    var hBox = new HBox(new Label(file.getName()));
    hBox.setSpacing(10);
    hBox.setPadding(new Insets(5));
    hBox.setAlignment(Pos.BASELINE_LEFT);

    var item = new TreeItem<Node>(hBox);

    itemGraphicBiMap.put(item, imageView);

    treeView.getRoot().getChildren().add(item);
    treeView.getSelectionModel().select(item);

    var up = new Button("↑");
    var down = new Button("↓");
    hBox.getChildren().add(0, down);
    hBox.getChildren().add(0, up);

    var del = new Button("❌");
    hBox.getChildren().add(del);

    up.setOnAction(_ -> moveUp(item));

    down.setOnAction(_ -> moveDown(item));

    del.setOnAction(_ -> del(item));

    sortPane();
  }

  public HBox createSVGPath(){
    return createSVGPath("Layer "+treeView.getRoot().getChildren().size());
  }

  public HBox createSVGPath(String name) {
    SVGPath svgPath = new SVGPath();

    SVGEditor2.getAppCast().center.getChildren().add(svgPath);

    var hBox = new HBox(new Label(name));
    hBox.setSpacing(10);
    hBox.setPadding(new Insets(5));
    hBox.setAlignment(Pos.BASELINE_LEFT);

    var item = new TreeItem<Node>(hBox);

    itemGraphicBiMap.put(item, svgPath);
    svgPathListMap.put(svgPath, new ArrayList<SVGPathElement>());

    treeView.getRoot().getChildren().add(0,item);
    treeView.getSelectionModel().select(item);

    var up = new Button("↑");
    var down = new Button("↓");
    hBox.getChildren().add(0, down);
    hBox.getChildren().add(0, up);

    var del = new Button("❌");
    hBox.getChildren().add(del);

    up.setOnAction(_ -> moveUp(item));

    down.setOnAction(_ -> moveDown(item));

    del.setOnAction(_ -> del(item));

    sortPane();

    return hBox;
  }

  private void sortPane(){
    SVGEditor2.getAppCast().center.getChildren().sort((n0,n1)->{
      var i0 = itemGraphicBiMap.inverse().get(n0);
      var i1 = itemGraphicBiMap.inverse().get(n1);
      if(i0==null || i1==null) return 1;
      var index0 = treeView.getRoot().getChildren().indexOf(i0);
      var index1 = treeView.getRoot().getChildren().indexOf(i1);
      return index1 - index0;
    });
  }

  private void moveUp(TreeItem<Node> item){
    var index = treeView.getRoot().getChildren().indexOf(item);
    if (index > 0) {
      treeView.getRoot().getChildren().add(index - 1, treeView.getRoot().getChildren().remove(index));
    }
    treeView.getSelectionModel().select(item);

    sortPane();
  }

  private void moveDown(TreeItem<Node> item){
    var index = treeView.getRoot().getChildren().indexOf(item);
    if (index >= 0 && index < treeView.getRoot().getChildren().size() - 1) {
      treeView.getRoot().getChildren().add(index + 1, treeView.getRoot().getChildren().remove(index));
    }
    treeView.getSelectionModel().select(item);

    sortPane();
  }

  private void del(TreeItem<Node> item){
    treeView.getRoot().getChildren().remove(item);
    switch(itemGraphicBiMap.remove(item)){
      case null -> {}
      case ImageView view -> SVGEditor2.getAppCast().center.getChildren().remove(view);
      case SVGPath path -> {
        SVGEditor2.getAppCast().center.getChildren().remove(path);
        svgPathListMap.get(path).forEach(e -> commandCircleMap.get(e).forEach(s -> SVGEditor2.getAppCast().center.getChildren().remove(s)));
      }
      default -> {}
    }
    sortPane();

  }

  public List<SVGPathElement> getSVGPathElements(SVGPath svgPath){
    return svgPathListMap.get(svgPath);
  }

  public Map<SVGPathElement, List<Shape>> getCommandCircleMap() {
    return commandCircleMap;
  }
}
