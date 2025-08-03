module com.whitewoodcity.svgeditor {
    requires javafx.controls;
    requires com.almasb.fxgl.all;

    opens com.whitewoodcity.svgeditor to javafx.fxml;
    exports com.whitewoodcity.svgeditor;
}