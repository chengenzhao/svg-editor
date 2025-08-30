package com.whitewoodcity.svgeditor;


import com.whitewoodcity.fxgl.vectorview.SVGLayer;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MenuBar extends javafx.scene.control.MenuBar {
  private MenuItem load = new MenuItem("Load");
  private MenuItem save = new MenuItem("Save");
  private MenuItem saveLayer = new MenuItem("Save current layer");

  public MenuBar() {
    var mainMenu = new Menu("Save&Load");
    mainMenu.getItems().addAll(saveLayer, save, load);
    getMenus().add(mainMenu);

    saveLayer.setOnAction(_ -> {
      var window = this.getScene().getWindow();

      var fileChooser = new FileChooser();
      fileChooser.setTitle("What file would you like to save?");
      fileChooser.setInitialFileName("layer.svgl");
      var file = fileChooser.showSaveDialog(window);
      if(file!=null && SVGEditor2.getAppCast().rightTree.currentNodeInPane() instanceof SVGLayer svgl){
        try{
          Files.write(Paths.get(file.getPath()), svgl.toJson().getBytes());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });

    load.setOnAction(_->{
      var window = this.getScene().getWindow();

      var fileChooser = new FileChooser();
      fileChooser.setTitle("What file would you like to load?");
      fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("svgl files","svgl"));
      var file = fileChooser.showOpenDialog(window);
      if(file!=null){
        try{

          var app = SVGEditor2.getAppCast();

          var json = Files.readString(Paths.get(file.getPath()));
          var item = app.rightTree.createSVGPath();
          var svgl = (SVGLayer)app.rightTree.getNodeByItem(item);
          svgl.fromJson(json);
          app.rightTree.treeView.getSelectionModel().select(item);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }
}
