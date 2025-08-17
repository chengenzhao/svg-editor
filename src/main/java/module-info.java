module com.whitewoodcity.svgeditor {
  requires javafx.controls;
  requires com.whitewoodcity.fxcity;
  requires javafx.graphics;
  requires javafx.base;
  requires atlantafx.base;
  requires javafx.fxml;
  requires com.almasb.fxgl.all;
  requires com.google.common;

  exports com.whitewoodcity.svgeditor;
  opens com.whitewoodcity.control.paintpicker to javafx.fxml;
  opens com.whitewoodcity.control.paintpicker.colorpicker to javafx.fxml;
  opens com.whitewoodcity.control.paintpicker.gradientpicker to javafx.fxml;
  opens com.whitewoodcity.control.paintpicker.images to javafx.fxml;
  opens com.whitewoodcity.control.paintpicker.rotator to javafx.fxml;
  opens com.whitewoodcity.control.paintpicker.slider to javafx.fxml;
}