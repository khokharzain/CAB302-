module com.example.newdesign {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.net.http;
    requires java.desktop;

    opens com.example.newdesign to javafx.fxml;
    exports com.example.newdesign;
}