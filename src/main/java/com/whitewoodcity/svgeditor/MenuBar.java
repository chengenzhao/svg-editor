package com.whitewoodcity.svgeditor;

import module java.base;
import module javafx.controls;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.whitewoodcity.control.NumberField;
import com.whitewoodcity.fxgl.vectorview.JVG;
import com.whitewoodcity.fxgl.vectorview.JVGLayer;
import javafx.scene.control.Label;

public class MenuBar extends javafx.scene.control.MenuBar {

  public MenuBar() {
    var mainMenu = new Menu("Files");
    MenuItem load = new MenuItem("Load");
    MenuItem saveAll = new MenuItem("Save all layers");
    MenuItem saveLayer = new MenuItem("Save current layer");
    MenuItem showImage = new MenuItem("Show snapshot image");

    var globalSetting = new Menu("Global Setting");
    var translation = new MenuItem("Translation");

    mainMenu.getItems().addAll(saveLayer, saveAll, load, showImage);
    globalSetting.getItems().addAll(translation);
    getMenus().addAll(mainMenu, globalSetting);

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

    showImage.setOnAction(_ -> {
      var im = new JVG(JVG.toJson(SVGEditor.getAppCast().center.getChildren()).toString()).toImage();

      Stage dialogStage = new Stage();
      dialogStage.initModality(Modality.WINDOW_MODAL);

      VBox vbox = new VBox(new ImageView(im));
      vbox.setAlignment(Pos.CENTER);
      vbox.setPadding(new Insets(15));

      dialogStage.setScene(new Scene(vbox));
      dialogStage.show();
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
      fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("jvg files", "*.jvgl", "*.jvg"));
      var file = fileChooser.showOpenDialog(window);
      if (file != null) {
        try {
          var ref = new TreeItemReference();
          var app = SVGEditor.getAppCast();
          var json = new ObjectMapper().readTree(Files.readString(Paths.get(file.getPath())));
          switch (json) {
            case ArrayNode arrayNode -> JVG.fromJson(obj -> {
              TreeItem<Node> item;
              if (obj.has(JVGLayer.JsonKeys.CLIP.key())) {
                var parent = ref.get();
                item = app.rightTree.createSVGPath("SubLayer" + parent.getChildren().size(), parent);
              } else {
                item = app.rightTree.createSVGPath();
                item.setExpanded(true);
                ref.set(item);
              }
              return (JVGLayer) app.rightTree.getNodeByItem(item);
            }, arrayNode);
            case ObjectNode objectNode -> {
              var item = app.rightTree.createSVGPath();
              var svgl = (JVGLayer) app.rightTree.getNodeByItem(item);
              svgl.fromJson(objectNode);
              app.rightTree.treeView.getSelectionModel().select(item);
            }
            default -> {
            }
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });

    translation.setOnAction(_ -> {
      var gridPane = new GridPane();
      gridPane.setPadding(new Insets(10));
      gridPane.setHgap(20);
      gridPane.setVgap(20);
      gridPane.add(new Label("Global translate X:"), 0, 0);
      gridPane.add(new Label("Global translate Y:"), 0, 1);

      var tranX = new NumberField(-(int) Screen.getPrimary().getBounds().getWidth(), (int) Screen.getPrimary().getBounds().getWidth());
      var tranY = new NumberField(-(int) Screen.getPrimary().getBounds().getHeight(), (int) Screen.getPrimary().getBounds().getHeight());
      gridPane.add(tranX, 1, 0);
      gridPane.add(tranY, 1, 1);

      var apply = new Button("Apply");
      gridPane.add(apply, 0, 2);
      var ok = new Button("OK");
      gridPane.add(ok, 1, 2);
      var scene = new Scene(gridPane);
      var stage = new Stage();
      stage.setScene(scene);
      stage.show();

      apply.setOnAction(_ -> {
        for (var node : SVGEditor.getAppCast().center.getChildren()) {
          if (node instanceof JVGLayer layer) {
            layer.move(tranX.getValue(), tranY.getValue());
            SVGEditor.getAppCast().updateSVGPath(layer);
          }
        }
        tranX.setText("0");
        tranY.setText("0");
      });
      ok.setOnAction(_ -> stage.close());
    });
  }
}
