module com.example.escritorioauditorio {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.escritorioauditorio to javafx.fxml;
    exports com.example.escritorioauditorio;
}