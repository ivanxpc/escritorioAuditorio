package com.example.escritorioauditorio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.regex.*;

public class loginController {

    @FXML
    private TextField usuario;
    @FXML
    private PasswordField contraseña;
    @FXML
    private TextField contraseñaTF;
    @FXML
    private CheckBox mostrar;
    @FXML
    private Button aceptar;
    @FXML
    private Button registrar;

    @FXML
    public void Aceptar(ActionEvent evt)throws Exception{

        Pattern patron = Pattern.compile("[A-Za-z0-9_.]*||(.*@[g][m][a][i][l][.][c][o][m])||" +
                                         "(.*@[c][h][a][p][a][l][a][.][t][e][c][m][m][.][e][d][u][.][m][x])");
        Matcher mat = patron.matcher(usuario.getText());

        if(mat.matches()) {
            ConexionLogin BD = new ConexionLogin();
            String Val_PASSWORD = contraseña.getText();
            String Val_USER= usuario.getText();
            BD.ConectarBasedeDatos();

            String SQL = "SELECT id FROM usuarios" +
                    " WHERE nombre='"+Val_USER+"'OR correo='"+Val_USER+"' AND password='"+Val_PASSWORD+"'";

            BD.resultado = BD.sentencia.executeQuery(SQL);

            if (BD.resultado.next()) {
                Stage s = (Stage) aceptar.getScene().getWindow();
                s.close();

                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Scene escena = new Scene(loader.load());
                stage.setScene(escena);
                stage.show();
            } else {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("errorLogin.fxml"));
                stage.setTitle("ERROR");
                Scene escena = new Scene(loader.load());
                stage.setScene(escena);
                stage.show();
            }
        }
    }

    @FXML
    void visible(ActionEvent evt){
        if(mostrar.isSelected()){
            contraseñaTF.setText(contraseña.getText());
            contraseñaTF.setVisible(true);
            contraseña.setVisible(false);
            return;
        }
        contraseña.setText(contraseñaTF.getText());
        contraseña.setVisible(true);
        contraseñaTF.setVisible(false);
    }

    @FXML
    public void Registrar(ActionEvent evt)throws Exception{
        Stage s = (Stage) registrar.getScene().getWindow();
        s.close();

        //ABRIR OTRA VENTANA
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
        Scene escena = new Scene(loader.load());
        stage.setScene(escena);
        stage.show();
    }
}
