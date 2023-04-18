package com.example.escritorioauditorio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Alerta_register {

    @FXML
    private Button aceptar;

    @FXML
    public void Cerrar(ActionEvent evt){
        Stage s = (Stage) aceptar.getScene().getWindow();
        s.close();
    }
}
