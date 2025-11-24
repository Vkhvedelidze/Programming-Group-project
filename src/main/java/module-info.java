module com.example.programminggroupproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.programminggroupproject to javafx.fxml;
    opens com.example.programminggroupproject.controller to javafx.fxml;
    
    exports com.example.programminggroupproject;
    exports com.example.programminggroupproject.controller;
    exports com.example.programminggroupproject.service;
}