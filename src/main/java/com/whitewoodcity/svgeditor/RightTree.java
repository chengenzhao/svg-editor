package com.whitewoodcity.svgeditor;

import module javafx.controls;
import module com.google.common;
import com.whitewoodcity.javafx.binding.XBindings;

import java.io.File;

public class RightTree extends VBox {

  public Button addVectorButton = new Button("Vector");
  public Button addBitmapButton = new Button("Bitmap");
  public TreeView<Node> treeView = new TreeView<>(new TreeItem<Node>());
  private BiMap<TreeItem, Node> itemGraphicBiMap = HashBiMap.create();

  public RightTree() {

    addVectorButton.setOnAction(_ -> createSVGPath());
    addBitmapButton.setOnAction(_ -> createImageView());

    var buttonBox = new HBox(new Label("+"), addVectorButton, addBitmapButton);
    buttonBox.setAlignment(Pos.BASELINE_LEFT);
    buttonBox.setSpacing(10);
    buttonBox.setPadding(new Insets(10));
    treeView.getRoot().getChildren().add(new TreeItem<>(new Label("Layer0")));
    treeView.setShowRoot(false);
    treeView.prefHeightProperty().bind(XBindings.reduceDoubleValue(this.heightProperty(), buttonBox.heightProperty(), (vh, rh) -> vh - rh));
    this.getChildren().addAll(buttonBox, treeView);

    treeView.getSelectionModel().selectFirst();

//    treeView.setOnMouseClicked(e -> {
//      if (e.getButton() == MouseButton.SECONDARY) {
//        treeView.getSelectionModel().clearSelection();
//        e.consume();
//      }
//    });
  }

  public TreeItem<Node> currentSelection(){
    return treeView.getSelectionModel().getSelectedItem();
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

    treeView.getRoot().getChildren().add(0, item);

    var up = new Button("↑");
    var down = new Button("↓");
    hBox.getChildren().add(0, down);
    hBox.getChildren().add(0, up);

    var del = new Button("❌");
    hBox.getChildren().add(del);

    up.setOnAction(_ -> moveUp(item));

    down.setOnAction(_ -> moveDown(item));

    del.setOnAction(_ -> del(item));
  }

  public void createSVGPath() {

  }

  private void moveUp(TreeItem<Node> item){
    var index = treeView.getRoot().getChildren().indexOf(item);
    if (index > 0) {
      treeView.getRoot().getChildren().add(index - 1, treeView.getRoot().getChildren().remove(index));
    }
    treeView.getSelectionModel().select(item);
  }

  private void moveDown(TreeItem<Node> item){
    var index = treeView.getRoot().getChildren().indexOf(item);
    if (index >= 0 && index < treeView.getRoot().getChildren().size() - 1) {
      treeView.getRoot().getChildren().add(index + 1, treeView.getRoot().getChildren().remove(index));
    }
    treeView.getSelectionModel().select(item);
  }

  private void del(TreeItem<Node> item){
    treeView.getRoot().getChildren().remove(item);
    SVGEditor2.getAppCast().center.getChildren().remove(itemGraphicBiMap.remove(item));
  }

}
