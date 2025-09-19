package com.whitewoodcity.svgeditor;


import module java.base;
import module javafx.controls;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.whitewoodcity.fxgl.vectorview.JVGLayer;
import com.whitewoodcity.fxgl.vectorview.JVG;

public class MenuBar extends javafx.scene.control.MenuBar {
  private final MenuItem load = new MenuItem("Load");
  private final MenuItem saveAll = new MenuItem("Save all layers");
  private final MenuItem saveLayer = new MenuItem("Save current layer");

  public MenuBar() {
    var mainMenu = new Menu("Files");
    mainMenu.getItems().addAll(saveLayer, saveAll, load);
    getMenus().add(mainMenu);

    saveAll.setOnAction(_ -> {
      var window = this.getScene().getWindow();

      var fileChooser = new FileChooser();
      fileChooser.setTitle("What file would you like to save?");
      fileChooser.setInitialFileName("layers.jvg");
      var file = fileChooser.showSaveDialog(window);
      if (file != null) {
        try {
          Files.write(Paths.get(file.getPath()),
            JVG.toJson(SVGEditor.getAppCast().center.getChildren()).toString().getBytes());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });

    saveLayer.setOnAction(_ -> {
      var window = this.getScene().getWindow();

      var fileChooser = new FileChooser();
      fileChooser.setTitle("What file would you like to save?");
      fileChooser.setInitialFileName("layer.jvgl");
      var file = fileChooser.showSaveDialog(window);
      if (file != null && SVGEditor.getAppCast().rightTree.currentNodeInPane() instanceof JVGLayer svgl) {
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
      fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("jvg files", "*.jvgl","*.jvg"));
      var file = fileChooser.showOpenDialog(window);
      if (file != null) {
        try {
          var ref = new TreeItemReference();
          var app = SVGEditor.getAppCast();
          var json = new ObjectMapper().readTree(Files.readString(Paths.get(file.getPath())));
          switch (json){
            case ArrayNode arrayNode-> {
              JVG.fromJson(obj -> {
                TreeItem<Node> item;
                if(obj.has(JVGLayer.JsonKeys.CLIP.key())){
                  var parent = ref.get();
                  item = app.rightTree.createSVGPath(parent);
                }else{
                  item = app.rightTree.createSVGPath();
                  item.setExpanded(true);
                  ref.set(item);
                }
                return (JVGLayer) app.rightTree.getNodeByItem(item);
              }, arrayNode);

            }
            case ObjectNode objectNode -> {
              var item = app.rightTree.createSVGPath();
              var svgl = (JVGLayer) app.rightTree.getNodeByItem(item);
              svgl.fromJson(objectNode);
              app.rightTree.treeView.getSelectionModel().select(item);
            }
            default -> {}
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }
}
