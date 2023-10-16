module test.demo.models {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires org.apache.poi.ooxml;
    requires org.apache.poi.poi;
    requires java.desktop;
    requires java.prefs;
    requires spring.core;
    requires mail;
    requires activation;

    opens test.demo to javafx.fxml;
    exports test.demo;
    exports test.demo.models;
    opens test.demo.models to javafx.fxml;
}