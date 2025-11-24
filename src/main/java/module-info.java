module com.example.programminggroupproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.programminggroupproject to javafx.fxml;
    exports com.example.programminggroupproject;
}