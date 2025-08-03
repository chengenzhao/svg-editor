package com.whitewoodcity.svgeditor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SVGEditor extends Application {
    @Override
    public void start(Stage stage){
        Scene scene = new Scene(new Button("test"), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}