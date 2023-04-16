package com.example.escritorioauditorio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class loginController {

    @FXML
    private TextField usuario;
    @FXML
    private TextField contraseña;
    @FXML
    private Button aceptar;

    @FXML
    public void Aceptar(ActionEvent evt)throws Exception{
        ConexionLogin BD = new ConexionLogin();
        String Val_PASSWORD = contraseña.getText();
        String Val_USER= usuario.getText();
        BD.ConectarBasedeDatos();

        String SQL = "SELECT id FROM usuarios" +
                " WHERE nombre='"+Val_USER+"' AND password='"+Val_PASSWORD+"'";
        BD.resultado = BD.sentencia.executeQuery(SQL);

        if(BD.resultado.next()){
            Stage s = (Stage) aceptar.getScene().getWindow();
            s.close();

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Scene escena = new Scene(loader.load());
            stage.setScene(escena);
            stage.show();
        }else{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("errorLogin.fxml"));
            stage.setTitle("ERROR");
            Scene escena = new Scene(loader.load());
            stage.setScene(escena);
            stage.show();
        }
    }
}
