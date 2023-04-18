package com.example.escritorioauditorio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    @FXML
    public void AgregarUsuarios(ActionEvent evt)throws Exception{

        Pattern patron_correo, patron_usuario, patron_contraseña;
        Matcher mat_correo, mat_usuario, mat_contaseña;

        patron_correo = Pattern.compile("(.*@[g][m][a][i][l][.][c][o][m])||" +
                "                        (.*@[c][h][a][p][a][l][a][.][t][e][c][m][m][.][e][d][u][.][m][x])");
        mat_correo = patron_correo.matcher(correo.getText());

        patron_usuario = Pattern.compile("[A-Za-z0-9_.]*");
        mat_usuario = patron_usuario.matcher(usuario.getText());

        patron_contraseña = Pattern.compile("[A-Za-z0-9_.]*");
        mat_contaseña = patron_contraseña.matcher(contraseña.getText());

        if(mat_usuario.matches() && mat_correo.matches() && mat_contaseña.matches()){
            Connection c = ConexionBD.getConexion();
            Statement stm = c.createStatement();
            String SQL = "INSERT INTO usuarios VALUES(0,'"+correo.getText()+ "','"+usuario.getText()+"','"
                    +contraseña.getText()+"')";
            stm.execute(SQL);

            correo.setText("");
            usuario.setText("");
            contraseña.setText("");

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Registrado.fxml"));
            stage.setTitle("REGISTRO CORRECTO");
            Scene escena = new Scene(loader.load());
            stage.setScene(escena);
            stage.showAndWait();
        }else{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UsuarioInvalido.fxml"));
            stage.setTitle("ERROR");
            Scene escena = new Scene(loader.load());
            stage.setScene(escena);
            stage.showAndWait();
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
