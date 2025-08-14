package com.whitewoodcity.svgeditor;

import module javafx.controls;
import com.whitewoodcity.javafx.binding.XBindings;

public class RightTree extends VBox {

  public RightTree() {
    var rootContent = new HBox(new Label("+"),new Button("Vector"), new Button("Bitmap"));
    rootContent.setAlignment(Pos.BASELINE_LEFT);
    rootContent.setSpacing(10);
    rootContent.setPadding(new Insets(10));
    var tree = new TreeView<Node>(new TreeItem<>());
    tree.getRoot().getChildren().add(new TreeItem<>(new Label("Layer0")));
    tree.setShowRoot(false);
    tree.prefHeightProperty().bind(XBindings.reduceDoubleValue(this.heightProperty(), rootContent.heightProperty(), (vh, rh) -> vh - rh));
    this.getChildren().addAll(rootContent, tree);
  }
}
