package com.whitewoodcity.svgeditor;


import module java.base;
import module javafx.controls;
import com.whitewoodcity.fxgl.vectorview.SVGLayer;
import com.whitewoodcity.fxgl.vectorview.VectorGraphics;

public class MenuBar extends javafx.scene.control.MenuBar {
  private MenuItem load = new MenuItem("Load");
  private MenuItem saveAll = new MenuItem("Save all layers");
  private MenuItem saveLayer = new MenuItem("Save current layer");

  public MenuBar() {
    var mainMenu = new Menu("Save&Load");
    mainMenu.getItems().addAll(saveLayer, saveAll, load);
    getMenus().add(mainMenu);

    saveAll.setOnAction(_ -> {
      var window = this.getScene().getWindow();

      var fileChooser = new FileChooser();
      fileChooser.setTitle("What file would you like to save?");
      fileChooser.setInitialFileName("layers.asvg");
      var file = fileChooser.showSaveDialog(window);
      if (file != null) {
        try {
          Files.write(Paths.get(file.getPath()),
            VectorGraphics.toJson(SVGEditor2.getAppCast().center.getChildren()).toString().getBytes());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });

    saveLayer.setOnAction(_ -> {
      var window = this.getScene().getWindow();

      var fileChooser = new FileChooser();
      fileChooser.setTitle("What file would you like to save?");
      fileChooser.setInitialFileName("layer.svgl");
      var file = fileChooser.showSaveDialog(window);
      if (file != null && SVGEditor2.getAppCast().rightTree.currentNodeInPane() instanceof SVGLayer svgl) {
        try {
          Files.write(Paths.get(file.getPath()), svgl.toJsonString().getBytes());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });

    load.setOnAction(_ -> {
      var window = this.getScene().getWindow();

      var fileChooser = new FileChooser();
      fileChooser.setTitle("What file would you like to load?");
      fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("svgl files", "svgl"));
      var file = fileChooser.showOpenDialog(window);
      if (file != null) {
        try {

          var app = SVGEditor2.getAppCast();

          var json = Files.readString(Paths.get(file.getPath()));
          var item = app.rightTree.createSVGPath();
          var svgl = (SVGLayer) app.rightTree.getNodeByItem(item);
          svgl.fromJson(json);
          app.rightTree.treeView.getSelectionModel().select(item);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }
}
