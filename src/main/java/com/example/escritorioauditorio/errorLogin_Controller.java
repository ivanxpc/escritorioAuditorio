package com.example.escritorioauditorio;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class errorLogin_Controller {
    @FXML
    private Button aceptar;

    @FXML
    private void CerrarVentana() {
        Stage s = (Stage) aceptar.getScene().getWindow();
        s.close();
    }
}
