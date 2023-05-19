package com.example.escritorioauditorio;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class cambiarCorreo{

    //MenuItem para cambio de correo

    @FXML
    public TextField correoEmisor;
    @FXML
    public TextField paswordEmisor;
    @FXML
    public TextField correoReceptor;
    @FXML
    private Button guardarDatosCorreo;

    //Variables para guardar los correos y password

    public String datosCorreo_Emisor;
    public String datospassword_Emisor;
    public String datosCorreo_Receptor;


    @FXML
    public void seleccionarCorreo_password(){
        /*datosCorreo_Emisor = (correoEmisor.getText());
        datospassword_Emisor = (paswordEmisor.getText());
        datosCorreo_Receptor = (correoReceptor.getText());

         */

        datosCorreo_Emisor = correoEmisor.getText();
        datospassword_Emisor = paswordEmisor.getText();
        datosCorreo_Receptor = correoReceptor.getText();


        System.out.println("Correo emisor: " + datosCorreo_Emisor);
        System.out.println("Password emisor: " + datospassword_Emisor);
        System.out.println("Correo recptor: " + datosCorreo_Receptor);
    }
}
