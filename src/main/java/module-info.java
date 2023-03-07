module com.example.escritorioauditorio {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.escritorioauditorio to javafx.fxml;
    exports com.example.escritorioauditorio;
}