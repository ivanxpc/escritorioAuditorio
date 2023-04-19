package com.example.escritorioauditorio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    /*
    //Alerta
    @FXML
    private Label labAlerta;
    @FXML
    private Label labAlertaContrasena;
    @FXML
    private Label labAlertaCorreo;
     */

    @FXML
    public void Aceptar(ActionEvent evt)throws Exception{

        //Pattern patron = Pattern.compile("[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.](mx||com)");
        //Matcher mat = patron.matcher(usuario.getText());


            ConexionLogin BD = new ConexionLogin();
            String Val_PASSWORD = contraseña.getText();
            String Val_USER= usuario.getText();
            BD.ConectarBasedeDatos();

            String SQL = "SELECT id FROM usuarios" +
                    " WHERE nombre='"+Val_USER+"'OR correo='"+Val_USER+"' AND password='"+Val_PASSWORD+"'";

            //QUERY PARA CONTRASENA
            String SQL2 = "SELECT id FROM usuarios WHERE password='"+Val_PASSWORD+"'";
            BD.resultado = BD.sentencia.executeQuery(SQL);

            if (BD.resultado.next()) {
                if (contraseña.getText() == "") {
                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("errorLogin.fxml"));
                    stage.setTitle("ERROR");
                    Scene escena = new Scene(loader.load());
                    stage.setScene(escena);
                    stage.show();
                }
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

/*
                System.out.println("contraseña o correo incorrecto");
                labAlerta.setVisible(true);
                labAlertaContrasena.setVisible(false);
                labAlertaCorreo.setVisible(false);

 */
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
