package com.example.escritorioauditorio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.regex.*;
import java.sql.Connection;
import java.sql.Statement;

public class registerController {
    @FXML
    private TextField usuario;
    @FXML
    private TextField correo;
    @FXML
    private TextField contraseña;
    @FXML
    private Button registrar;
    @FXML
    private Button volver;

    //Alertas
    @FXML
    private Label labAlerta;

    @FXML
    public void AgregarUsuarios(ActionEvent evt)throws Exception{
        try {
            Pattern patron_correo, patron_usuario, patron_contraseña;
            Matcher mat_correo, mat_usuario, mat_contaseña;

            patron_correo = Pattern.compile("[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.](mx||com||edu)");
            mat_correo = patron_correo.matcher(correo.getText());

            patron_usuario = Pattern.compile("\\D[A-Za-z0-9]{4,15}");
            mat_usuario = patron_usuario.matcher(usuario.getText());

            patron_contraseña = Pattern.compile("[A-Za-z0-9_]{5,20}");
            mat_contaseña = patron_contraseña.matcher(contraseña.getText());

            if (mat_usuario.matches() && mat_correo.matches() && mat_contaseña.matches()) {
                    Connection c = ConexionBD.getConexion();
                    Statement stm = c.createStatement();
                    String SQL = "INSERT INTO usuarios VALUES(0,'" + usuario.getText() + "','" + correo.getText() + "','"
                            + contraseña.getText() + "')";
                    stm.execute(SQL);

                    correo.setText("");
                    usuario.setText("");
                    contraseña.setText("");

                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Registrado.fxml"));
                    stage.setTitle("REGISTRO CORRECTO");
                    Scene escena = new Scene(loader.load());
                    stage.setScene(escena);
                    stage.setResizable(false);
                    stage.showAndWait();

                    Stage CV = (Stage) registrar.getScene().getWindow();
                    CV.close();

                    Stage ST = new Stage();
                    FXMLLoader load = new FXMLLoader(getClass().getResource("login.fxml"));
                    Scene sc = new Scene(load.load());
                    ST.setScene(sc);
                    ST.show();
            } else {
                labAlerta.setVisible(true);
                labAlerta.setText("Formato de correo no valido");
                //Thread.sleep(5000);
                //labAlerta.setText("");
                //labAlertaContrasena.setVisible(false);
                //labAlertaCorreo.setVisible(false);
            }
        }catch (Exception e){
            System.out.println("Error");
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("errorConexionF.fxml"));
            Scene escena = new Scene(loader.load());
            stage.setScene(escena);
            stage.show();
        }
    }

    @FXML
    public void Volver(ActionEvent evt)throws Exception{
        Stage s = (Stage) volver.getScene().getWindow();
        s.close();

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene escena = new Scene(loader.load());
        stage.setScene(escena);
        stage.show();
    }
}
