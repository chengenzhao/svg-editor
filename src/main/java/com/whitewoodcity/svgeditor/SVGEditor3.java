package com.whitewoodcity.svgeditor;

import module javafx.controls;
import module javafx.fxml;
import com.whitewoodcity.control.paintpicker.PaintPickerController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * poc paint picker
 * we directly copy & paste the code from scene builder kit
 * then make a simple test here
 */
public class SVGEditor3 extends Application {
  @Override
  public void start(Stage primaryStage) {
    VBox root = new VBox();
    root.setAlignment(Pos.CENTER);
    root.setPadding(new Insets(50));

    PaintPickerController controller;
    final FXMLLoader loader = new FXMLLoader();
    loader.setLocation(PaintPickerController.class.getResource("PaintPicker.fxml"));

    try {
      final VBox picker = loader.load();
      controller = loader.getController();
      controller.paintProperty().addListener((_, _, nv) -> IO.println("Paint: " + nv));
      root.getChildren().add(picker);
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }

    Scene scene = new Scene(root, 600, 800);

    primaryStage.setTitle("SceneBuilder PaintPicker");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
