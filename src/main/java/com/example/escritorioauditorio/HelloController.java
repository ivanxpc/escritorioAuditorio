package com.example.escritorioauditorio;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import javafx.util.Callback;

public class HelloController {
    //TAB

    @FXML
    private Tab folioTab;
    @FXML
    private Tab pdf;
    @FXML
    private Tab fechasDisponibles;
    @FXML
    private TabPane tabGeneral;

    //Fechas Disponibles

    @FXML
    private TextField Nota;
    DatePicker fechas = new DatePicker();
    @FXML
    private DatePicker Calendario = fechas ;
    @FXML
    private Button btn_Agendar;
    @FXML
    private CheckBox Sillas;
    @FXML
    private CheckBox Tablones;
    @FXML
    private CheckBox Mesas;
    @FXML
    private CheckBox Microfonos;
    @FXML
    private CheckBox Podium;
    @FXML
    private CheckBox Baños;
    @FXML
    private CheckBox Agua;
    @FXML
    private CheckBox Manteleria;
    @FXML
    private CheckBox Personas1;
    @FXML
    private CheckBox Personas2;
    @FXML
    private CheckBox Personas3;
    @FXML
    private CheckBox Personas4;
    @FXML
    private CheckBox Horario1;
    @FXML
    private CheckBox Horario2;
    @FXML
    private CheckBox Horario3;
    @FXML
    private CheckBox Horario4;

    //PDF adjuntar

    @FXML
    private Button descargar;
    @FXML
    private Button subir;
    @FXML
    private Button adjuntar;
    @FXML
    private Button adjuntar1;
    @FXML
    private ImageView archivo1;
    @FXML
    private Button btn_archivo1;
    @FXML
    private ImageView archivo2;
    @FXML
    private Button btn_archivo2;
    @FXML
    private Label archivo1L;
    @FXML
    private Label archivo2L;
    @FXML
    private Label espereEnviar;

    //Folio

    @FXML
    private TableView tabla;
    @FXML
    private TableColumn id;
    @FXML
    private TableColumn nombre;
    @FXML
    private TableColumn primerA;
    @FXML
    private TableColumn segundoA;
    @FXML
    private TableColumn cargo;
    @FXML
    private TableColumn area;
    @FXML
    private TableColumn tipoUsuario;
    @FXML
    private TableColumn contacto;
    @FXML
    private TableColumn motivo;
    @FXML
    private TableColumn fPrestamo;
    @FXML
    private TableColumn fAgenda;
    @FXML
    private TableColumn folio;
    @FXML
    private Button actualizarS;
    @FXML
    private TextField buscar;
    @FXML
    private Button continuar;
    @FXML
    private Label error_Conexion;

    //sesion
    @FXML
    private MenuItem cerrar_s;
    @FXML
    private Button cerrar_Sesion;

    //TAB Cambiar Correos

    @FXML
    private TextField correoEmisor;
    @FXML
    private TextField paswordEmisor;
    @FXML
    private TextField correoReceptor;
    @FXML
    private Button guardarDatosCorreo;

    //Variables para guardar los correos y password

    public static String datosCorreo_Emisor;
    public static String datospassword_Emisor;
    public static String datosCorreo_Receptor;



    //MenuBar
    @FXML
    public void cerrarApp(ActionEvent evt) {
        Platform.exit();
    }

    private datos_usuario temporalSolicitantes;

    private  ObservableList<datos_usuario> bd_usuarioDatos = FXCollections.observableArrayList();
    Document documento = new Document();


    hiloActualizar_Tabla hiloTabla = new hiloActualizar_Tabla();
    //hiloCalendario hiloCalendario = new hiloCalendario();
    hiloBuscar hiloBuscar = new hiloBuscar();
    loginController hiloLogin = new loginController();

    String correoEmisorF;
    String passwordEmisorF;
    String correoReceptorF;

    public void initialize(){
        nuevoMetodoCal();
        buscarSolicitantes();

         hiloBuscar.start();
        //Metodo para leer el archivo creado
         correoEmisorF =  deserializarObjeto("EstadoCorreoE.Dat", String.class);
        System.out.println("Correo Emisor: "+correoEmisorF);
         passwordEmisorF =  deserializarObjeto("EstadoPasswordE.Dat", String.class);
        System.out.println("Password Emisor: "+passwordEmisorF);
         correoReceptorF =  deserializarObjeto("EstadoCorreoR.Dat", String.class);
        System.out.println("Correo Receptor: "+correoReceptorF);


        //Esto es tambien del calendario Date piker




    }



    //Declaracion de Threads


    private class hiloBuscar extends Thread {
        @Override
        public void run() {
            while (true) {
                // Pero usamos un truco y hacemos un ciclo infinito
                try {
                    // En él, hacemos que el hilo duerma
                    Thread.sleep(10000);
                    // Y después realizamos las operaciones
                    // System.out.println("Me imprimo cada segundo");

                    System.out.println("Hilo buscar");
                    buscarSolicitantes();

                    if (buscar.getText().trim().length() >= 1){
                        hiloTabla.interrupt();
                        System.out.println("Hilo tabla parado");
                        if (hiloTabla.isInterrupted()){
                            System.out.println("Alto final");
                        }else {
                            //hiloTabla.start();
                        }
                        /*if (hiloTabla.isAlive()){
                            hiloTabla.interrupt();
                            //buscarSolicitantes();
                            System.out.println("Alto hilo tabla");
                        }else {
                            //hiloTabla.start();
                            System.out.println("No hizo nada");
                        }*/
                    } /*else if (buscar.getText().trim().length() != 1) {
                        //hiloTabla.start();

                    }
                    */



                    // Así, se da la impresión de que se ejecuta cada cierto tiempo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



        }

    }

    private class hiloActualizar_Tabla extends Thread {
        @Override
        public void run() {
            actualizarDatos();

           /* while (true) {


                // Pero usamos un truco y hacemos un ciclo infinito
                try {

                    // En él, hacemos que el hilo duerma
                    Thread.sleep(1000);



                    if (buscar.getText().trim().length() >= 1){
                        hiloTabla.interrupt();
                        System.out.println("Hilo tabla parado hilooo");
                    }
                    if (hiloTabla.isInterrupted()){
                        // hiloTabla.interrupt();
                        System.out.println("Se ha detenido el proceso de actualizar");

                    }else {
                        // Esto se ejecuta en segundo plano una única vez
                        while (true) {
                            // Pero usamos un truco y hacemos un ciclo infinito
                            try {
                                actualizarDatos();
                                // En él, hacemos que el hilo duerma
                                Thread.sleep(5000);
                                // Y después realizamos las operaciones
                                System.out.println("Me imprimo cada segundo");
                                // Así, se da la impresión de que se ejecuta cada cierto tiempo
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
            //hiloTabla.interrupt();



        }


    }



    //  ESTO ES UNA COPIA

    /*
     @FXML
    public void metodoCalendario(){

        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {


            // public void updateItem(LocalDate item, boolean empty) {
            public void updateItem(LocalDate item, boolean empty) {

                System.out.println("Metodo updateItem!!");
                super.updateItem(item, empty);


                this.setDisable(false);
                this.setBackground(null);
                this.setTextFill(Color.BLACK);



                int day ;
                int month;
                int year;

                //String valor;
                try {
                    Connection c = ConexionBD.getConexion();
                    Statement stm = c.createStatement();
                    String sql = "SELECT fechaAgenda FROM datosusuario where fechaAgenda is not null";
                    ResultSet r = stm.executeQuery(sql);
                    System.out.println("Conexion");
                    //stm.execute(sql);

                    while(r.next()){
                        System.out.println("Metodo while");
                        day = item.getDayOfMonth();
                        month = item.getMonthValue();
                        year = item.getYear();
                        //ESTE METODO ES PARA MARCAR LOS DIAS EN DATEPICKER DE LA BASE DE DATOS

                        if (year == Integer.parseInt(r.getString("fechaAgenda").split("-")[0]) && month == Integer.parseInt(r.getString("fechaAgenda").split("-")[1]) && day == Integer.parseInt(r.getString("fechaAgenda").split("-")[2])) {
                            Paint color = Color.RED;
                            System.out.println("Metodo if");
                            BackgroundFill fill = new BackgroundFill(color, null, null);
                            this.setBackground(new Background(fill));
                            this.setTextFill(Color.WHITESMOKE);

                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }



                //updateSelected(true);
                //updater.accept(item);
            }




        };
        Calendario.setDayCellFactory(dayCellFactory);

    }
     */


    ResultSet r;
    @FXML
    public void nuevoMetodoCal() {


        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {

            public void updateItem(LocalDate item, boolean empty) {

                super.updateItem(item, empty);

                this.setDisable(false);
                this.setBackground(null);
                this.setTextFill(Color.BLACK);

                int day;
                int month;
                int year;
                int entrada =1;
                int es;
                es = item.getDayOfMonth();

                //System.out.println("Imprimir");

                // marcar los dias de quincena



                  /*day = item.getDayOfMonth();
                  month = item.getMonthValue();
                  year = item.getYear();

                   */
                if (entrada == 1) {



                    try {
                        ConexionBD.getConexion();
                        Statement stm = ConexionBD.c.createStatement();
                        String sql = "SELECT fechaAgenda FROM datosusuario where fechaAgenda is not null";
                        ResultSet r = stm.executeQuery(sql);
                        System.out.println("Conexion");
                        //stm.execute(sql);


                        while (r.next()) {

                            System.out.println("Metodo while");
                            day = item.getDayOfMonth();
                            month = item.getMonthValue();
                            year = item.getYear();

                            //ESTE METODO ES PARA MARCAR LOS DIAS EN DATEPICKER DE LA BASE DE DATOS

                            if (year == Integer.parseInt(r.getString("fechaAgenda").split("-")[0]) && month == Integer.parseInt(r.getString("fechaAgenda").split("-")[1]) && day == Integer.parseInt(r.getString("fechaAgenda").split("-")[2])) {
                                Paint color = Color.RED;
                                System.out.println("Metodo if");
                                BackgroundFill fill = new BackgroundFill(color, null, null);
                                this.setBackground(new Background(fill));
                                this.setTextFill(Color.WHITESMOKE);


                            }

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }








                //if(es == 0) {


                    //if 1
               // }




            }

        };
        Calendario.setDayCellFactory(dayCellFactory);

    }


    @FXML public void intentoCalendario() {

        //fAgenda.getText().trim().length();
        //System.out.println(fAgenda);
        // this.setDisable(false);
        //this.setBackground(null);
        //this.setTextFill(Color.BLACK);
        int day ;
        int month;
        int year;




        try {
            Connection c = ConexionBD.getConexion();
            Statement stm = c.createStatement();
            String sql = "SELECT fechaAgenda FROM datosusuario where fechaAgenda is not null";
            ResultSet r = stm.executeQuery(sql);
            System.out.println("Conexion");
            //stm.execute(sql);

            while(r.next()){
                /*day = item.getDayOfMonth();
                month = item.getMonthValue();
                year = item.getYear();

                 */
                String imprimir = r.getString("fechaAgenda").split("-")[0];
                String imprimir2 = r.getString("fechaAgenda").split("-")[1];
                String imprimir3= r.getString("fechaAgenda").split("-")[2];
                System.out.println(imprimir + " " + imprimir2 + " " + imprimir3);

                //System.out.println(day + " " + month + " " + year);

                day = Integer.parseInt(imprimir3);
                month = Integer.parseInt(imprimir2);
                year = Integer.parseInt(imprimir3);
                //ESTE METODO ES PARA MARCAR LOS DIAS EN DATEPICKER DE LA BASE DE DATOS
               /* if (imprimir == imprimir){
                    Paint color = Color.RED;
                    BackgroundFill fill = new BackgroundFill(color, null, null);

                    this.setBackground(new Background(fill));
                    this.setTextFill(Color.WHITESMOKE);

                }

                */

                if (year == Integer.parseInt(r.getString("fechaAgenda").split("-")[0]) && month == Integer.parseInt(r.getString("fechaAgenda").split("-")[1]) && day == Integer.parseInt(r.getString("fechaAgenda").split("-")[2])) {
                    Paint color = Color.RED;
                    System.out.println("Metodo if");
                    BackgroundFill fill = new BackgroundFill(color, null, null);


                    //this.setBackground(new Background(fill));
                    //this.setTextFill(Color.WHITESMOKE);

                }

                //System.out.println(r.getString("fechaAgenda").split("-")[0]) && Integer.parseInt(r.getString("fechaAgenda").split("-")[1]) &&  Integer.parseInt(r.getString("fechaAgenda").split("-")[2]));

                //ESTE METODO ES PARA MARCAR LOS DIAS EN DATEPICKER DE LA BASE DE DATOS

               /* if (year == Integer.parseInt(r.getString("fechaAgenda").split("-")[0]) && month == Integer.parseInt(r.getString("fechaAgenda").split("-")[1]) && day == Integer.parseInt(r.getString("fechaAgenda").split("-")[2])) {
                    Paint color = Color.RED;
                    System.out.println("Metodo if");
                    BackgroundFill fill = new BackgroundFill(color, null, null);
                    this.setBackground(new Background(fill));
                    this.setTextFill(Color.WHITESMOKE);

                }

                */
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        /*int fila = Integer.parseInt(fAgenda.getText());
        int i;

        String valor = null;
        for (i = 0; i < fila; i++) {
            valor = (String) fAgenda.getText(i, 0);
        }
        String valores = valor + ", ";
        JOptionPane.showMessageDialog(null, "valores de la columna1: " + valores);

         */
    }







    @FXML
    public void actualizarSolicitantes(){
        actualizarDatos();
    }


//Actualizar datos de la tabla
    public void actualizarDatos(){
        try {
            ConexionBD.getConexion();
            Statement stm = ConexionBD.c.createStatement();
            String sql = "SELECT * FROM datosusuario";
            ResultSet r = stm.executeQuery(sql);
            bd_usuarioDatos.clear();
            while (r.next()) {
                tabla.setItems(bd_usuarioDatos);
                bd_usuarioDatos.add(new datos_usuario(
                        r.getInt("ID"),
                        r.getString("Nombre"),
                        r.getString("apellidoP"),
                        r.getString("apellidoM"),
                        r.getString("Cargo"),
                        r.getString("Area"),
                        r.getString("tipoSolicitante"),
                        r.getString("Motivo"),
                        r.getString("fecha"),
                        r.getString("Contacto"),
                        r.getString("fechaAgenda")));

                id.setCellValueFactory(new PropertyValueFactory<>("id"));
                nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                primerA.setCellValueFactory(new PropertyValueFactory<>("apellidoP"));
                segundoA.setCellValueFactory(new PropertyValueFactory<>("apellidoM"));
                cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
                area.setCellValueFactory(new PropertyValueFactory<>("area"));
                tipoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipoSolicitante"));
                motivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
                fPrestamo.setCellValueFactory(new PropertyValueFactory<>("fecha"));
                contacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
                fAgenda.setCellValueFactory(new PropertyValueFactory<>("fechaAgenda"));

            }
            stm.execute(sql);
            System.out.println("DATOS ACTUALIZADOS");
            error_Conexion.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error de conexion a internet");
        }
        tabla.refresh();

    }

    @FXML
    public void CerrarSesion(){
        try {
            Stage test = (Stage) cerrar_Sesion.getScene().getWindow();
            test.close();

            Stage stage = new Stage();//Crear una nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene escena = new Scene(loader.load());
            stage.setResizable(false);
            stage.setTitle("LOGIN");
            stage.setScene(escena);//agregar la esena a la ventana
            stage.showAndWait();

            ConexionLogin BD = new ConexionLogin();
            BD.DesconectarBasedeDatos();
            System.out.println("SESION CERRADA");
        } catch (Exception d){

        }
    }
    @FXML
    public void MostrarCreditos()throws Exception{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Creditos.fxml"));
        Scene escena = new Scene(loader.load());
        stage.setResizable(false);
        stage.setTitle("Créditos");
        stage.setResizable(false);
        stage.setScene(escena);
        stage.showAndWait();
    }
    @FXML
    public void AbrirManualdeUso()throws Exception{
        String direccion = System.getProperty("user.dir")+"\\src\\main\\resources\\com\\example\\escritorioauditorio\\image/MANUAL DE USUARIO.pdf";
        ProcessBuilder archivo = new ProcessBuilder();
        archivo.command("cmd.exe","/c",direccion);
        archivo.start();
    }
    @FXML
    public void descargarArchivo()throws Exception {
        //Esta es mi dirrrecion de Ivan PC
        //String File_name = "C:\\Users\\nalai\\OneDrive\\Escritorio\\Formulario.pdf";

        //String ruta = System.getProperty("user.home");
         //ruta = "C:\\Users\\nalai\\Downloads\\Formulario.pdf";
        //String ruta = System.getProperty("user.home");
        String ruta = "C:\\Users\\"+System.getProperty("user.name")+"\\Downloads\\Formulario.pdf";

        //String File_name = "C:\\Users\\nalai\\OneDrive\\Escritorio\\Formulario.pdf";

        //Esta es mi dirrrecion de Ivan PC
        Image imagen = Image.getInstance(System.getProperty("user.dir")+"\\src\\main\\resources\\com\\example\\escritorioauditorio\\image/PDF.jpg");
        //Image imagen = Image.getInstance("C:\\Users\\nalai\\IdeaProjects\\escritorioAuditorio\\src\\main\\resources\\com\\example\\escritorioauditorio\\image/PDF.jpg");
        imagen.scaleToFit(600,1100);
        imagen.setAlignment(Chunk.ALIGN_JUSTIFIED);

        Document Descarga_PDF = new Document();
        PdfWriter.getInstance(Descarga_PDF, new FileOutputStream(ruta));
        //PdfWriter.getInstance(Descarga_PDF, new FileOutputStream(File_name));

        Descarga_PDF.open();
        Descarga_PDF.add(imagen);
        Descarga_PDF.close();

        try {
            Stage stage = new Stage();//Crear una nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("descargaPDF.fxml"));
            Scene escena = new Scene(loader.load());
            stage.setResizable(false);
            stage.setTitle("PDF DESCARGADO");
            stage.setScene(escena);//agregar la escena a la ventana
            stage.setResizable(false);
            stage.showAndWait();
        } catch (Exception d){

        }

        }

    //Metodo para ir a la Tab siguente
    public void siguiente(){
            tabGeneral.getSelectionModel().select(1);
    }

    File datos;
    File datos1;
    FileChooser fileChooser = new FileChooser();
    //Este metodo es para adjuntar los archivos al correo Button adjuntar
    @FXML
    public void adjunatarArechivo_C(){

            datos = fileChooser.showOpenDialog(null);

            fileChooser.setTitle("Buscar archivo");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("archivos de texto", "*.pdf"));
            //File ruta = new File(String.valueOf(fileChooser));

            // Archivo 1
            if (datos == null) {
                try {
                    Stage stage = new Stage();//Crear una nueva ventana
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("errorAdjuntar.fxml"));
                    stage.setResizable(false);
                    Scene escena = new Scene(loader.load());
                    stage.setTitle("ARCHIVO ADJUNTO");
                    stage.setScene(escena);//agregar la escena a la ventana
                    stage.setResizable(false);
                    stage.showAndWait();
                } catch (Exception d) {
                    //System.out.println(d + "No se ha adjuntado ningun archivo");

                }

            }else {
                try {
                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("exitoAdjuntar_Archivo.fxml"));
                    Scene escena = new Scene(loader.load());
                    stage.setResizable(false);
                    stage.setTitle("ARCHIVO ADJUNTO");
                    stage.setScene(escena);
                    stage.setResizable(false);
                    stage.showAndWait();
                } catch (Exception d) {
                    //System.out.println(d + "No se ha adjuntado ningun archivo");

                }

            }

            if (datos != null){
                btn_archivo1.setDisable(false);
                archivo1L.setText(String.valueOf(datos));
                archivo1.setVisible(true);

            }
    }
    @FXML
    public void adjuntarArchivo_C1(){

        datos1 = fileChooser.showOpenDialog(null);

        //Archivo 2
        if (datos1 == null) {
            try {
                Stage stage = new Stage();//Crear una nueva ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("errorAdjuntar.fxml"));
                stage.setResizable(false);
                Scene escena = new Scene(loader.load());
                stage.setTitle("ERROR");
                stage.setScene(escena);//agregar la escena a la ventana
                stage.setResizable(false);
                stage.showAndWait();
            } catch (Exception d) {

            }

        }else {
            try {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("exitoAdjuntar_Archivo.fxml"));
                Scene escena = new Scene(loader.load());
                stage.setResizable(false);
                stage.setTitle("ARCHIVO ADJUNTO");
                stage.setScene(escena);
                stage.setResizable(false);
                stage.showAndWait();
            } catch (Exception d) {

            }

        }

        if (datos1 != null){
            btn_archivo2.setDisable(false);
            archivo2L.setText(String.valueOf(datos1));
            archivo2.setVisible(true);

        }

    }

//Metodo para eliminar los archivo1 seleccionado
    @FXML
    public void deleteFile(ActionEvent evt) {

        if (datos != null) {
            datos = null;
            System.out.println("Archivo eliminado");
            btn_archivo1.setDisable(true);
            archivo1L.setText("");
            archivo1.setVisible(false);
        }

    }

    //Metodo para eliminar los archivo2 seleccionado
    @FXML
    public void deleteFile1(ActionEvent evt) {

        if(datos1 != null) {
            datos1 = null;
            System.out.println("Archivo eliminado");
            btn_archivo2.setDisable(true);
            archivo2L.setText("");
            archivo2.setVisible(false);

        }


    }

    //Nuevo metodo para enviar por correo metodo 2
    /*@FXML
    public void test(){

        final String userName = "ipadillacordova@gmail.com"; //same fromMail
        final String password = "kxljyvdjmmqvypad";
        final String toEmail = "ipadillacordova@gmail.com";

        System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        // props.put("mail.smtp.ssl.enable", "true");
        // props.put("mail.smtp.socketFactory.port", "587"); //TLS Port

        //Session session = Session.getDefaultInstance(props);
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("ipadillacordova@gmail.com", "kxljyvdjmmqvypad");

            }
        });

        try{
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("nalaivan123@hotmail.com", true));
            message.setSubject("Prueba");
            message.setText("Blablabla");
            System.out.println("sending...");
            Transport.send(message);
            System.out.println("Sent message successfully....");

        }catch (MessagingException me){
            System.out.println("Exception: "+me);

        }
    }

     */



//Metodo para enviar por correo los archivos
    @FXML
    public void enviarArchivo_C(){
        System.out.println("Valor datos: "+datos);
        System.out.println("Valor datos1: "+datos1);


        try {

            Properties propiedades = new Properties();

            propiedades.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            propiedades.put("mail.smtp.port", "587"); //TLS Port
            propiedades.put("mail.smtp.auth", "true"); //enable authentication
            propiedades.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
            propiedades.put("mail.smtp.ssl.protocols", "TLSv1.2");

            //Este tambien funciono anteriormente
            /*propiedades.setProperty("mail.smtp.host", "smtp.gmail.com");
            propiedades.setProperty("mail.smtp.port", "465");
            propiedades.setProperty("mail.smtp.auth", "true");
            propiedades.setProperty("mail.smtp.starttls.enable", "true");
            propiedades.setProperty("mail.smtp.starttls.required", "true");
            propiedades.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
            propiedades.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

             */

            Session sesion = Session.getDefaultInstance(propiedades);

            //Este si funciona
            //String correo_emisor = "ipadillacordova@gmail.com";
            //String password_emisor = "kxljyvdjmmqvypad";
            String correo_emisor = correoEmisorF;
            String password_emisor = passwordEmisorF;




            //String correo_receptor = "ch200110782@chapala.tecmm.edu.mx";
            /*String correo_receptor = "ipadillacordova@gmail.com";

            String mensaje = "Hola me llamo Ivan Padilla ";
            String asunto = "Esto es una prueba";

             */



            String correo_receptor = correoReceptorF;
            //String correo_receptor = "ipadillacordova@gmail.com";
            String asunto = "Archivos del Solicitante";
            String mensaje = "Solicitud y formulario <br> Auditorio Tec de Chapala <br> <i>Archivos del solicitante: </i>";

            BodyPart texto = new MimeBodyPart();
            texto.setContent(mensaje,"text/html");

            if (datos == null | datos1 == null){

                System.out.println("Error al enviar los archivos");

                    /*espereEnviar.setVisible(false);
                    subir.setDisable(false);

                     */



                try {
                    Stage stage = new Stage();//Crear una nueva ventana
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("errorCargaAr.fxml"));
                    stage.setResizable(false);
                    Scene escena = new Scene(loader.load());
                    stage.setScene(escena);
                    stage.showAndWait();
                } catch (Exception d){

                }


            }else {


                //Archivo1
                BodyPart adjuntos = new MimeBodyPart();
                //adjuntos.setDataHandler(new DataHandler(new FileDataSource("C:\\Users\\nalai\\OneDrive\\Escritorio/pdf.png"))); //Aqui pones la direccion de tu imagen
                adjuntos.setDataHandler(new DataHandler(new FileDataSource(datos)));
                adjuntos.setFileName("Archivo1.pdf");
                //imagen.setDataHandler(new DataHandler(new FileDataSourse("C:\\Users\\nalai\\OneDrive\\Escritorio/pdf.png")));
                System.out.println("Archivo adjuntado!!");
                MimeMultipart partes = new MimeMultipart();
                partes.addBodyPart(texto);
                partes.addBodyPart(adjuntos);


                //Archivo2
                BodyPart adjuntos1 = new MimeBodyPart();
                //adjuntos.setDataHandler(new DataHandler(new FileDataSource("C:\\Users\\nalai\\OneDrive\\Escritorio/pdf.png"))); //Aqui pones la direccion de tu imagen
                adjuntos1.setDataHandler(new DataHandler(new FileDataSource(datos1)));
                adjuntos1.setFileName("Archivo2.pdf");
                //imagen.setDataHandler(new DataHandler(new FileDataSourse("C:\\Users\\nalai\\OneDrive\\Escritorio/pdf.png")));
                System.out.println("Archivo adjuntado!!");
                MimeMultipart partes1 = new MimeMultipart();
                partes1.addBodyPart(texto);
                partes1.addBodyPart(adjuntos1);






                //Correo de archivo1
                MimeMessage message = new MimeMessage(sesion);
                message.setFrom(new InternetAddress(correo_emisor));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(correo_receptor));
                message.setSubject(asunto);
                //message.setText(mensaje);
                message.setContent(partes);

                Transport transporte = sesion.getTransport("smtp");
                transporte.connect(correo_emisor,password_emisor);
                transporte.sendMessage(message , message.getRecipients(Message.RecipientType.TO));
                transporte.close();

                //Correo de archivo2
                MimeMessage message1 = new MimeMessage(sesion);
                message1.setFrom(new InternetAddress(correo_emisor));
                message1.addRecipient(Message.RecipientType.TO, new InternetAddress(correo_receptor));
                message1.setSubject(asunto);
                message1.setContent(partes1);

                Transport transporte1 = sesion.getTransport("smtp");
                transporte1.connect(correo_emisor,password_emisor);
                transporte1.sendMessage(message1 , message1.getRecipients(Message.RecipientType.TO));
                transporte1.close();


                System.out.println("El mensaje se ha enviado  . ");
                ;

                try {
                    Stage stage = new Stage();//Crear una nueva ventana
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("enviarArchivo.fxml"));
                    stage.setResizable(false);
                    Scene escena = new Scene(loader.load());
                    stage.setTitle("ARCHIVOS ENVIADOS");
                    stage.setScene(escena);//agregar la esena a la ventana
                    stage.showAndWait();
                } catch (Exception d){

                }

            }


        } catch (AddressException ex) {
            System.out.println("Error en el correo" + ex);
            try {
                Stage stage = new Stage();//Crear una nueva ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("errorCoreoA.fxml"));
                stage.setResizable(false);
                Scene escena = new Scene(loader.load());
                stage.setTitle("ARCHIVO ENVIADO");
                stage.setScene(escena);//agregar la esena a la ventana
                stage.showAndWait();
            } catch (Exception d){

            }
            //JOptionPane.showMessageDialog(null,"Error : " +ex);
        } catch (MessagingException ex) {
            //JOptionPane.showMessageDialog(null,"Error : " +ex);
            System.out.println("Error en el mensaje " + ex);
            try {
                Stage stage = new Stage();//Crear una nueva ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("errorMensajeA.fxml"));
                stage.setResizable(false);
                Scene escena = new Scene(loader.load());
                stage.setTitle("ARCHIVO ENVIADO");
                stage.setScene(escena);//agregar la esena a la ventana
                stage.showAndWait();
            } catch (Exception d){

            }
        }

    }


    //METODO PARA SELECCIONAR EN LA TABLA SOLICITANTES
    int idSolicitantes;
    @FXML
    public void ClickTablaSolicitantes(MouseEvent evt){
        if(evt.getClickCount() >= 1){
                datos_usuario p = (datos_usuario) tabla.getSelectionModel().getSelectedItem();
                temporalSolicitantes = p;
                idSolicitantes = p.getId();
            System.out.println("Se ha seleccionado el solicitante");
            System.out.println("ID: " + p.getId());
            continuar.setDisable(false);

        }

    }



    //ACCION PARA BUSCAR SOLICITANTES

    @FXML
    public void buscarSolicitantes() {
        //Connection c = ConexionBD.getConexion();
        try {
            ConexionBD.getConexion();
            Statement stm = ConexionBD.c.createStatement();
            String sql = "SELECT * FROM datosusuario WHERE nombre LIKE '" + buscar.getText() + "%'";
            ResultSet r = stm.executeQuery(sql);
            bd_usuarioDatos.clear();
            while (r.next()) {
                tabla.setItems(bd_usuarioDatos);
                bd_usuarioDatos.add(new datos_usuario(
                        r.getInt("id"),
                        r.getString("nombre"),
                        r.getString("apellidoP"),
                        r.getString("apellidoM"),
                        r.getString("cargo"),
                        r.getString("area"),
                        r.getString("tipoSolicitante"),
                        r.getString("motivo"),
                        r.getString("fecha"),
                        r.getString("contacto"),
                        r.getString("fechaAgenda")));


                id.setCellValueFactory(new PropertyValueFactory<>("id"));
                nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                primerA.setCellValueFactory(new PropertyValueFactory<>("apellidoP"));
                segundoA.setCellValueFactory(new PropertyValueFactory<>("apellidoM"));
                cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
                area.setCellValueFactory(new PropertyValueFactory<>("area"));
                tipoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipoSolicitante"));
                motivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
                fPrestamo.setCellValueFactory(new PropertyValueFactory<>("fecha"));
                contacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
                fAgenda.setCellValueFactory(new PropertyValueFactory<>("fechaAgenda"));
            }
            stm.execute(sql);
            error_Conexion.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
            error_Conexion.setVisible(true);
        }
    }
    //Metodo para generar PDF

    @FXML
    public void metodoGenerar_PDF() {
        // documento = new Document(PageSize.A4, 35, 30 ,50 ,50);
        Document documento = new Document();
        //documento.setPageSize(PageSize.A4.rotate());
        //documento.setMargins(15, 15, 15, 15);
        try {
            Connection c = ConexionBD.getConexion();
            Statement stm = c.createStatement();
            //UPDATE datosusuario SET fechaAgenda = "2023/04/23" WHERE id = 3
            String sql = "UPDATE datosusuario SET fechaAgenda = '"+ Calendario.getValue() + "'  WHERE id = " + idSolicitantes;
            System.out.println("DATOS INSERTADOS");
            stm.execute(sql);
            //Se lo agrege stm.close****
            stm.close();
            actualizarDatos();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e+ "No se inserto datos!!\n");
        }

        try {


           // String ruta = System.getProperty("user.home");
            //C:\Users\nalai\OneDrive\Escritorio
            //PdfWriter.getInstance(documento, new FileOutputStream(  "C:\\Users\\nalai\\OneDrive\\Escritorio/Auditorio.pdf"));
            PdfWriter.getInstance(documento, new FileOutputStream(  "C:\\Users\\"+System.getProperty("user.name")+"\\Downloads/Auditorio.pdf"));
            //PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Videos/Auditorio.pdf"));
            //Insertar image

            //Image logo = Image.getInstance("C:\\Users\\nalai\\IdeaProjects\\escritorioAuditorio\\src\\main\\resources\\com\\example\\escritorioauditorio\\image/logoTecMM.png");
            Image logo = Image.getInstance(System.getProperty("user.dir")+"\\src\\main\\resources\\com\\example\\escritorioauditorio\\image/logoTecMM.png");
            logo.scaleToFit(250,250);
            logo.setAlignment(Chunk.ALIGN_CENTER);

            //Image bordo = Image.getInstance("C:\\Users\\nalai\\IdeaProjects\\escritorioAuditorio\\src\\main\\resources\\com\\example\\escritorioauditorio\\image/triangulosMorados.png");
            Image bordo = Image.getInstance(System.getProperty("user.dir")+"\\src\\main\\resources\\com\\example\\escritorioauditorio\\image/triangulosMorados.png");
            bordo.scaleToFit(300,300);
            logo.setAlignment(Chunk.ALIGN_BOTTOM);

            //Insertar texto
            Paragraph parrafo = new Paragraph();
            Paragraph contacto = new Paragraph();
            Paragraph motivo = new Paragraph();
            Paragraph fecha = new Paragraph();
            Paragraph espacio = new Paragraph();
            Paragraph separacion = new Paragraph();

            //Moviliario
            Paragraph mobiliario = new Paragraph();
            Paragraph sillas = new Paragraph();
            Paragraph tablones = new Paragraph();
            Paragraph mesas = new Paragraph();
            Paragraph microfonos = new Paragraph();
            Paragraph podium = new Paragraph();
            Paragraph banos = new Paragraph();
            Paragraph agua = new Paragraph();
            Paragraph manteleria = new Paragraph();

            //Personas
            Paragraph personas = new Paragraph();
            Paragraph per50 = new Paragraph();
            Paragraph per100 = new Paragraph();
            Paragraph per200 = new Paragraph();
            Paragraph per300 = new Paragraph();

            //Horario
            Paragraph horario = new Paragraph();
            Paragraph h1 = new Paragraph();
            Paragraph h2 = new Paragraph();
            Paragraph h3 = new Paragraph();
            Paragraph h4 = new Paragraph();

            //Nota
            Paragraph texto = new Paragraph();

            //datepiker

            Paragraph fechaDP = new Paragraph();

            parrafo.setFont(FontFactory.getFont("Tahoma", 18, Font.BOLD, BaseColor.BLACK));
            parrafo.add("Registro Agenda del Auditorio \n\n");
            parrafo.setAlignment(Paragraph.ALIGN_CENTER);
            parrafo.add("Datos del Solicitante: \n\n");

            documento.open();
            documento.add(logo);
            documento.add(bordo);
            documento.add(parrafo);


            //PdfPTable tabla = new PdfPTable(5);

            //tabla.SetWidths(values);

            PdfPTable tabla = new PdfPTable(8);
            float[] values = new float[8];
            values[0] = 70;
            values[1] = 200;
            values[2] = 200;
            values[3] = 200;

            values[4] = 200;
            values[5] = 200;
            values[6] = 150;
            values[7] = 150;

           /* values[7] = 250;
            values[8] = 250;
            values[9] = 250;*/
            //values[10] = 75;

            tabla.addCell("ID");
            tabla.addCell("Nombre");
            tabla.addCell("Primer Apellido");
            tabla.addCell("Segundo Apellido");
            tabla.addCell("Cargo");
            tabla.addCell("Area");
            tabla.addCell("Tipo de Usuario");
            tabla.addCell("Folio");
            //tabla.addCell("Contacto");
            //tabla.addCell("Motivo");
            //tabla.addCell("Fecha de Prestamo");
            tabla.setWidths(values);
            //tabla.SetWidths(values);
            // tabla.addCell("Numero de Personas");
            //tabla.addCell("Folio");

            try {
                Connection cn = DriverManager.getConnection("jdbc:mysql://65.99.252.253:3306/eduwitco_auditorio","eduwitco_auditorio","Ivan.098&$%");
                //Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/auditorio","root","");
                //PreparedStatement pst = cn.prepareStatement("SELECT * FROM datosusuario");
                PreparedStatement pst = cn.prepareStatement("SELECT * FROM datosusuario WHERE id= " + idSolicitantes );

                ResultSet rs = pst.executeQuery();
                if (rs.next()){

                    do {

                        tabla.addCell(rs.getString(1));
                        tabla.addCell(rs.getString(2));
                        tabla.addCell(rs.getString(3));
                        tabla.addCell(rs.getString(4));
                        tabla.addCell(rs.getString(5));
                        tabla.addCell(rs.getString(6));
                        tabla.addCell(rs.getString(7));
                        tabla.addCell(rs.getString(1));
                        //tabla.addCell(rs.getString(10));
                        //tabla.addCell(rs.getString(8));
                        //tabla.addCell(rs.getString(9));
                        espacio.add("\n" );
                        contacto.add("                       Contacto:   "+rs.getString(10));
                        motivo.add("                       Motivo:   "+rs.getString(8));
                        fecha.add("                       Fecha de elaboración de solicitud:   "+rs.getString(9));
                        fechaDP.add("                       Fecha de Agenda:   " + rs.getString(11));
                        espacio.add("\n");

                        //Moviliario
                        mobiliario.add("                       Mobiliario seleccionado:\n");
                        if (Sillas.isSelected()) {
                            sillas.add( "                       Sillas\n");
                        }else if (Sillas.isDisabled()){

                        }
                        if (Tablones.isSelected()) {
                            tablones.add( "                       Tablones\n");
                        }else if (Tablones.isDisabled()){

                        }
                        if (Mesas.isSelected()) {
                            mesas.add( "                       Mesas\n");
                        }else if (Mesas.isDisabled()){

                        }
                        if (Microfonos.isSelected()) {
                            microfonos.add( "                       Micrófonos\n");
                        }else if (Microfonos.isDisabled()){

                        }
                        if (Podium.isSelected()) {
                            podium.add( "                       Podium\n");
                        }else if (Podium.isDisabled()){

                        }
                        if (Baños.isSelected()) {
                            banos.add( "                       Baños\n");
                        }else if (Baños.isDisabled()){

                        }
                        if (Agua.isSelected()) {
                            agua.add( "                       Agua\n");
                        }else if (Agua.isDisabled()){

                        }
                        if (Manteleria.isSelected()) {
                            manteleria.add( "                       Mantelería\n");
                        }else if (Manteleria.isDisabled()){

                        }



                        //espacio.add("\n");

                        //Personas
                        personas.add("                       Cantidad de personas: \n");
                        if (Personas1.isSelected()) {
                            per50.add( "                        50 - 100 \n");
                        }else if (Personas1.isDisabled()){

                        }
                        if (Personas2.isSelected()) {
                            per100.add( "                       100 - 200 \n");
                        }else if (Personas2.isDisabled()){

                        }
                        if (Personas3.isSelected()) {
                            per200.add( "                       200 - 300 \n");
                        }else if (Personas3.isDisabled()){

                        }
                        if (Personas4.isSelected()) {
                            per300.add( "                       300 - 500 \n");
                        }else if (Personas4.isDisabled()){

                        }

                        //espacio.add("\n");


                        //Horario
                        horario.add("                       Duración del evento: \n");
                        if (Horario1.isSelected()) {
                            h1.add( "                       30min - 1h \n");
                        }else if (Horario1.isDisabled()){

                        }
                        if (Horario2.isSelected()) {
                            h2.add( "                       1:00h - 1:30h \n");
                        }else if (Horario2.isDisabled()){

                        }
                        if (Horario3.isSelected()) {
                            h3.add( "                       1:30h - 2:00h \n");
                        }else if (Horario3.isDisabled()){

                        }
                        if (Horario4.isSelected()) {
                            h4.add( "                       otro \n");
                        }else if (Horario4.isDisabled()){

                        }
                        separacion.add("                       ------------------------------------ \n");

                        //Nota
                        texto.add("                        Descripción de otro elemento extra: "+Nota.getText());

                        //parrafo.add(rs.getString(8));
                        //tabla.addCell(rs.getString(11));
                        //tabla.addCell(rs.getString(12));
                    }while (rs.next());
                    documento.add(tabla);
                    documento.add(espacio);
                    documento.add(contacto);
                    documento.add(motivo);
                    documento.add(fecha);
                    documento.add(fechaDP);
                    documento.add(espacio);
                    documento.add(separacion);

                    documento.add(mobiliario);
                    documento.add(sillas);
                    documento.add(tablones);
                    documento.add(mesas);
                    documento.add(microfonos);
                    documento.add(podium);
                    documento.add(banos);
                    documento.add(agua);
                    documento.add(manteleria);
                    //documento.add(espacio);
                    documento.add(separacion);


                    documento.add(personas);
                    documento.add(per50);
                    documento.add(per100);
                    documento.add(per200);
                    documento.add(per300);
                    //documento.add(espacio);
                    documento.add(separacion);

                    documento.add(horario);
                    documento.add(h1);
                    documento.add(h2);
                    documento.add(h3);
                    documento.add(h4);
                    documento.add(separacion);

                    documento.add(texto);
                    documento.add(separacion);


                }

            }catch (DocumentException | SQLException e){
                System.out.println("Error en generar PDF" + e);
            }

            documento.close();
            System.out.println("Documento creado");

            try {
                Stage stage = new Stage();//Crear una nueva ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("crearPDF.fxml"));
                stage.setResizable(false);
                Scene escena = new Scene(loader.load());
                stage.setTitle("PDF CREADO CORRECTAMENTE");
                stage.setResizable(false);
                stage.setScene(escena);//agregar la esena a la ventana
                stage.showAndWait();
                tabGeneral.getSelectionModel().select(2);
            } catch (Exception d){

            }

        }catch (DocumentException | FileNotFoundException e){

            try {
                Stage stage = new Stage();//Crear una nueva ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("error_PDF.fxml"));
                stage.setResizable(false);
                Scene escena = new Scene(loader.load());
                stage.setTitle("ERROR");
                stage.setResizable(false);
                stage.setScene(escena);//agregar la esena a la ventana
                stage.showAndWait();
            } catch (Exception d){

            }

            System.out.println("Error en generar PDFfffff" + e);

        }catch (IOException e){
            System.out.println("Error en la imagen" + e);
        }
    }
    //Metodo para asiganar las variables a los datos de correo
    @FXML
    public void seleccionarCorreo_password() {
        datosCorreo_Emisor = (correoEmisor.getText());
        datospassword_Emisor = (paswordEmisor.getText());
        datosCorreo_Receptor = (correoReceptor.getText());

        //System.out.println("Correo emisor: " + datosCorreo_Emisor);
        //System.out.println("Password emisor: " + datospassword_Emisor);
        //System.out.println("Correo recptor: " + datosCorreo_Receptor);

        serializarObjeto("EstadoCorreoE.Dat",correoEmisor.getText());
        serializarObjeto("EstadoPasswordE.Dat",paswordEmisor.getText());
        serializarObjeto("EstadoCorreoR.Dat",correoReceptor.getText());

        //Metodo para leer el archivo creado
        correoEmisorF =  deserializarObjeto("EstadoCorreoE.Dat", String.class);
        System.out.println("Correo Emisor: "+correoEmisorF);
        passwordEmisorF =  deserializarObjeto("EstadoPasswordE.Dat", String.class);
        System.out.println("Password Emisor: "+passwordEmisorF);
        correoReceptorF =  deserializarObjeto("EstadoCorreoR.Dat", String.class);
        System.out.println("Correo Receptor: "+correoReceptorF);

        try {
            Stage stage = new Stage();//Crear una nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("exitoAdjuntar_Archivo.fxml"));
            stage.setResizable(false);
            Scene escena = new Scene(loader.load());
            stage.setTitle("ERROR");
            stage.setResizable(false);
            stage.setScene(escena);//agregar la esena a la ventana
            stage.showAndWait();
            correoEmisor.setText("");
            paswordEmisor.setText("");
            correoReceptor.setText("");
        } catch (Exception d){

        }




    }

    public static boolean serializarObjeto(String direccionArchivo, Serializable objeto) {
        boolean sw = false;
        try (FileOutputStream fos = new FileOutputStream(direccionArchivo);
             ObjectOutputStream salida = new ObjectOutputStream(fos);) {
            salida.writeObject(objeto);
            sw = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sw;
    }

    public static <E> E deserializarObjeto(String direccionArchivo, Class<E> claseObjetivo) {
        E objeto = null;
        try (FileInputStream fis = new FileInputStream(direccionArchivo);
             ObjectInputStream entrada = new ObjectInputStream(fis);) {
            objeto = (E) entrada.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objeto;
    }


}