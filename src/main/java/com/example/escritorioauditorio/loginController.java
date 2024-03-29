package com.example.escritorioauditorio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.util.regex.*;

public class loginController extends Thread{

    @FXML
    private TextField usuario;
    @FXML
    private PasswordField contraseña;
    @FXML
    private TextField contraseñaTF;
    @FXML
    private ImageView mostrar;
    @FXML
    private ImageView ocultar;
    @FXML
    private Button aceptar;
    @FXML
    private Button registrar;

    //Alerta
    @FXML
    private Label labAlerta;
    @FXML
    private Label labAlertaContrasena;
    @FXML
    private Label labAlertaCorreo;
    @FXML
    private Label labAlertaEsperar;



        public void run(){
            try {
                Aceptar();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            visible();
        }

    //Metodo para hacer login
    @FXML
    public void Aceptar()throws Exception{
        try {

            ConexionLogin BD = new ConexionLogin();
            String Val_PASSWORD = contraseña.getText();
            String Val_USER = usuario.getText();
            BD.ConectarBasedeDatos();


            String SQL = "SELECT id FROM usuarios" +
                    " WHERE nombre='" + Val_USER + "' AND password= '" + Val_PASSWORD + "'";
            BD.resultado = BD.sentencia.executeQuery(SQL);

            if (BD.resultado.next()) {
                aceptar.setDisable(true);

                Stage s = (Stage) aceptar.getScene().getWindow();
                s.close();

                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                stage.setResizable(false);
                Scene escena = new Scene(loader.load());
                stage.setTitle("Agendas del Auditorio");
                stage.setScene(escena);
                stage.show();
                aceptar.setDisable(true);
                labAlertaEsperar.setText("Espere un momento...");
            } else {
                aceptar.setVisible(true);
                labAlerta.setVisible(true);
                labAlerta.setText("Usuario o contraseña incorrecto(a)");
                //Thread.sleep(5000);
                //labAlerta.setText("");
                labAlertaContrasena.setVisible(false);
                labAlertaCorreo.setVisible(false);
                labAlertaEsperar.setText("");
                aceptar.setDisable(false);
            }

        }catch (Exception e){

            System.out.println(e+"error");
            Stage stage = new Stage();//Crear una nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("errorConexionF.fxml"));
            stage.setResizable(false);
            Scene escena = new Scene(loader.load());
            stage.setScene(escena);//agregar la esena a la ventana
            stage.showAndWait();

        }
    }

    @FXML
    public void visible(){
        if (ocultar.isVisible()){
            ocultar.setVisible(false);
            mostrar.setVisible(true);
            contraseñaTF.setVisible(true);
            contraseña.setVisible(false);
            contraseñaTF.setText(contraseña.getText());
        }else if (mostrar.isVisible()) {
            mostrar.setVisible(false);
            ocultar.setVisible(true);
            contraseñaTF.setVisible(false);
            contraseña.setVisible(true);
            contraseña.setText(contraseñaTF.getText());
        }
    }

    @FXML
    public void Registrar(ActionEvent evt)throws Exception{
        Stage s = (Stage) registrar.getScene().getWindow();
        s.close();

        //ABRIR OTRA VENTANA
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
        stage.setResizable(false);
        Scene escena = new Scene(loader.load());
        stage.setScene(escena);
        stage.show();
    }
}
