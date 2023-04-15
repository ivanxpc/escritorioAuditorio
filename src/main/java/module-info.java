module com.example.escritorioauditorio {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires google.cloud.firestore;
    requires firebase.admin;
    requires com.google.auth.oauth2;
    requires itextpdf;


    opens com.example.escritorioauditorio to javafx.fxml;
    exports com.example.escritorioauditorio;
    exports Dominio;
    opens Dominio to javafx.fxml;

}