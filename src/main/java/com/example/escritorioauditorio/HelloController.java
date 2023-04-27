package com.example.escritorioauditorio;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
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

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
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
    @FXML
    private DatePicker Calendario;
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

    //PDF

    @FXML
    private Button descargar;
    @FXML
    private Button subir;
    @FXML
    private Button adjuntar;
    @FXML
    private ImageView archivo1;
    @FXML
    private Button btn_archivo1;
    @FXML
    private ImageView archivo2;
    @FXML
    private Button btn_archivo2;

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
    private Accordion accordionSolicitantes;

    //sesion
    @FXML
    private MenuItem cerrar_s;
    @FXML
    private Button cerrar_Sesion;

    //MenuBar
    @FXML
    public void cerrarApp(ActionEvent evt) {
        Platform.exit();
    }

    private datos_usuario temporalSolicitantes;

    private  ObservableList<datos_usuario> bd_usuarioDatos = FXCollections.observableArrayList();
    Document documento = new Document();




    public void initialize(){
        actualizarDatos();
        actualizarSolicitantes();
        //DatePicker fecha = new DatePicker();
        Calendario.setDayCellFactory(dayCellFactory);
    }
    DatePicker fecha = new DatePicker();

    Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {

        public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);

            this.setDisable(false);
            this.setBackground(null);
            this.setTextFill(Color.BLACK);

            // deshabilitar las fechas futuras
            /*if (item.isAfter(LocalDate.now())) {
                this.setDisable(true);
            }

             */

            int day ;
            int month;
            int year;

            //day == Calendario.getValue().getDayOfMonth())
            //fecha.setOnAction(e -> System.out.println("fecha: " +  fecha.getValue()));
            //fAgenda.getText();
            //System.out.println(fAgenda);

            String valor;
            try {
                Connection c = ConexionBD.getConexion();
                Statement stm = c.createStatement();
                String sql = "SELECT fechaAgenda FROM datosusuario where fechaAgenda is not null";
                ResultSet r = stm.executeQuery(sql);
                //stm.execute(sql);

                while(r.next()){
                        day = item.getDayOfMonth();
                        month = item.getMonthValue();
                        year = item.getYear();
                        //ESTE METODO ES PARA MARCAR LOS DIAS EN DATEPICKER DE LA BASE DE DATOS

                        if (year == Integer.parseInt(r.getString("fechaAgenda").split("-")[0]) && month == Integer.parseInt(r.getString("fechaAgenda").split("-")[1]) && day == Integer.parseInt(r.getString("fechaAgenda").split("-")[2])) {
                            Paint color = Color.RED;
                            BackgroundFill fill = new BackgroundFill(color, null, null);
                            this.setBackground(new Background(fill));
                            this.setTextFill(Color.WHITESMOKE);
                        }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }



            // fines de semana en color verde
            DayOfWeek dayweek = item.getDayOfWeek();
            if (dayweek == DayOfWeek.SATURDAY || dayweek == DayOfWeek.SUNDAY) {
                this.setTextFill(Color.GREEN);
            }
        }
    };



    @FXML
    public void actualizarSolicitantes(){
        actualizarDatos();
    }
//Actualizar datos de la tabla
    public void actualizarDatos(){

        try {
            Connection c = ConexionBD.getConexion();
            Statement stm = c.createStatement();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        tabla.refresh();

    }

    @FXML
    public void CerrarSesion(){

        //HelloApplication.setVista("login");

        try {

            Stage test = (Stage) cerrar_Sesion.getScene().getWindow();
            test.close();

            Stage stage = new Stage();//Crear una nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene escena = new Scene(loader.load());
            stage.setTitle("Finalizado");
            stage.setScene(escena);//agregar la esena a la ventana
            stage.showAndWait();

        } catch (Exception d){

        }

    }
    @FXML
    public void descargarArchivo()throws Exception {
        //Esta es mi dirrrecion de Ivan PC
        //String File_name = "C:\\Users\\nalai\\OneDrive\\Escritorio\\Formulario.pdf";
        String File_name = "C:\\Users\\nalai\\OneDrive\\Escritorio\\Formulario.pdf";


        //Esta es mi dirrrecion de Ivan PC
        //Image imagen = Image.getInstance("C:\\Users\\nalai\\IdeaProjects\\escritorioAuditorio\\src\\main\\resources\\com\\example\\escritorioauditorio\\image/PDF.jpg");
        Image imagen = Image.getInstance("C:\\Users\\nalai\\IdeaProjects\\escritorioAuditorio\\src\\main\\resources\\com\\example\\escritorioauditorio\\image/PDF.jpg");
        imagen.scaleToFit(600,1100);
        imagen.setAlignment(Chunk.ALIGN_JUSTIFIED);

        Document Descarga_PDF = new Document();
        PdfWriter.getInstance(Descarga_PDF, new FileOutputStream(File_name));

        Descarga_PDF.open();
        Descarga_PDF.add(imagen);
        Descarga_PDF.close();

        try {
            Stage stage = new Stage();//Crear una nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("descargaPDF.fxml"));
            Scene escena = new Scene(loader.load());
            stage.setTitle("Finalizado");
            stage.setScene(escena);//agregar la esena a la ventana
            stage.showAndWait();
        } catch (Exception d){

        }

        }

    //Tab siguente
    public void siguiente(){
            tabGeneral.getSelectionModel().select(1);
    }

    //Metodo para enviar los archivos por correo
    public void enviarArechivos(){
        try {
            Properties propiedades = new Properties();
            /*
            propiedades.setProperty("mail.smtp.host", "smtp.gmail.com");
            propiedades.setProperty("mail.smtp.starttls.enable", "true");
            propiedades.setProperty("mail.smtp.port", "587");
            propiedades.setProperty("mail.smtp.auth", "true");

             */
            propiedades.setProperty("mail.smtp.host", "smtp.gmail.com");
            propiedades.setProperty("mail.smtp.port", "465");
            propiedades.setProperty("mail.smtp.auth", "true");
            propiedades.setProperty("mail.smtp.starttls.enable", "true");
            propiedades.setProperty("mail.smtp.starttls.required", "true");
            propiedades.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
            propiedades.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session sesion = Session.getDefaultInstance(propiedades);

            String correo_emisor = "ipadillacordova@gmail.com";
            String password_emisor = "rideprwxpzwbjpqc";

            //String correo_receptor = "ch200110782@chapala.tecmm.edu.mx";
            /*String correo_receptor = "ipadillacordova@gmail.com";

            String mensaje = "Hola me llamo Ivan Padilla ";
            String asunto = "Esto es una prueba";

             */
            String correo_receptor = "ipadillacordova@gmail.com";
            String asunto = "Java";
            String mensaje = "Hola , me llamo christian Ramirez";

            MimeMessage message = new MimeMessage(sesion);
            message.setFrom(new InternetAddress(correo_emisor));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(correo_receptor));
            message.setSubject(asunto);
            message.setText(mensaje);

            Transport transporte = sesion.getTransport("smtp");
            transporte.connect(correo_emisor,password_emisor);
            transporte.sendMessage(message , message.getRecipients(Message.RecipientType.TO));
            transporte.close();

            System.out.println("El mensaje se ha enviado  . ");


        } catch (AddressException ex) {
            System.out.println("Error en el correo" + ex);
            //JOptionPane.showMessageDialog(null,"Error : " +ex);
        } catch (MessagingException ex) {
            //JOptionPane.showMessageDialog(null,"Error : " +ex);
            System.out.println("Error en el mensaje" + ex);
        }

    }

File datos;
    //Este metodo es para adjuntar los archivos al correo
    @FXML
    public void adjunatarArechivo_C(){
        /*btn_archivo1.setDisable(false);
        btn_archivo2.setDisable(false);
        archivo1.setDisable(false);
        archivo2.setDisable(false);

         */

        File archivo = new File("Archivos");
        System.out.println(archivo.getAbsolutePath());
        System.out.println(archivo.exists());

        FileChooser fileChooser = new FileChooser();
         datos = fileChooser.showOpenDialog(null);

        fileChooser.setTitle("Buscar archivo");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("archivos de texto", "*.pdf"));
        //File ruta = new File(String.valueOf(fileChooser));


        if (datos == null) {
            System.out.println("No has seleccionado ningun archivo");


        }
        System.out.println(datos);

        try {
            Stage stage = new Stage();//Crear una nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("exitoAdjuntar_Archivo.fxml"));
            Scene escena = new Scene(loader.load());
            stage.setTitle("Finalizado");
            stage.setScene(escena);//agregar la esena a la ventana
            stage.showAndWait();
        } catch (Exception d){

        }

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(datos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);

    }

   /* public void eliminarArchivoSubido(){
        datos.delete();
    }

    */


    public boolean deleteFile(String file) {

        File f = new File(file);

        if (f.delete()) {
            System.out.println("File " + file + " is deleted");
            return true;
        }

        System.out.println("Error deleting file: " + file);
        return false;
    }


//Metodo para enviar por correo los archivos
    @FXML
    public void enviarArchivo_C(){

        try {
            Properties propiedades = new Properties();


            /*
            propiedades.setProperty("mail.smtp.host", "smtp.gmail.com");
            propiedades.setProperty("mail.smtp.starttls.enable", "true");
            propiedades.setProperty("mail.smtp.port", "587");
            propiedades.setProperty("mail.smtp.auth", "true");

             */
            propiedades.setProperty("mail.smtp.host", "smtp.gmail.com");
            propiedades.setProperty("mail.smtp.port", "465");
            propiedades.setProperty("mail.smtp.auth", "true");
            propiedades.setProperty("mail.smtp.starttls.enable", "true");
            propiedades.setProperty("mail.smtp.starttls.required", "true");
            propiedades.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
            propiedades.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session sesion = Session.getDefaultInstance(propiedades);

            String correo_emisor = "ipadillacordova@gmail.com";
            String password_emisor = "rideprwxpzwbjpqc";

            //String correo_receptor = "ch200110782@chapala.tecmm.edu.mx";
            /*String correo_receptor = "ipadillacordova@gmail.com";

            String mensaje = "Hola me llamo Ivan Padilla ";
            String asunto = "Esto es una prueba";

             */
            String correo_receptor = "ipadillacordova@gmail.com";
            String asunto = "Archivos del Solicitante";
            //String mensaje = "Hola , me llamo christian Ramirez";
            String mensaje = "Solicitud y formulario <br> Auditorio Tec de Chapala <br> <i>Esto es una prueba</i>";

            BodyPart texto = new MimeBodyPart();
            texto.setContent(mensaje,"text/html");

            BodyPart adjuntos = new MimeBodyPart();
            //adjuntos.setDataHandler(new DataHandler(new FileDataSource("C:\\Users\\nalai\\OneDrive\\Escritorio/pdf.png"))); //Aqui pones la direccion de tu imagen
            adjuntos.setDataHandler(new DataHandler(new FileDataSource(datos)));
            adjuntos.setFileName("Archivo.pdf");
            //imagen.setDataHandler(new DataHandler(new FileDataSourse("C:\\Users\\nalai\\OneDrive\\Escritorio/pdf.png")));
            System.out.println("Archivo adjuntado!!");
            MimeMultipart partes = new MimeMultipart();
            partes.addBodyPart(texto);
            partes.addBodyPart(adjuntos);

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

            System.out.println("El mensaje se ha enviado  . ");

            try {
                Stage stage = new Stage();//Crear una nueva ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("enviarArchivo.fxml"));
                Scene escena = new Scene(loader.load());
                stage.setTitle("Finalizado");
                stage.setScene(escena);//agregar la esena a la ventana
                stage.showAndWait();
            } catch (Exception d){

            }




        } catch (AddressException ex) {
            System.out.println("Error enel correo" + ex);
            //JOptionPane.showMessageDialog(null,"Error : " +ex);
        } catch (MessagingException ex) {
            //JOptionPane.showMessageDialog(null,"Error : " +ex);
            System.out.println("Error en el mensaje" + ex);
        }

    }


    //METODO PARA SELECCIONAR EN LA TABLA SOLICITANTES
    int idSolicitantes;
    @FXML
    public void ClickTablaSolicitantes(MouseEvent evt){
        if(evt.getClickCount() >= 2){
                datos_usuario p = (datos_usuario) tabla.getSelectionModel().getSelectedItem();
                temporalSolicitantes = p;
                idSolicitantes = p.getId();
            System.out.println("Se ha seleccionado el solicitante");
            System.out.println("ID: " + p.getId());
            continuar.setDisable(false);

        }

    }

    //Para cargar tabla automaticamente

   /* @FXML
    private void cargarTabla() {
        try {
            Connection miComando = ConexionBD.getConexion();
            CallableStatement obtenerClientes = miComando.prepareCall("SELECT * FROM datosusuario");
            ResultSet rs = obtenerClientes.executeQuery();
            //Obtiene información sobre los tipos y las propiedades de las columnas de un ResultSet.
            ResultSetMetaData rsmd = rs.getMetaData();
            //String Titulo[] = {"Nro", "Nombre", "Apellido", "Domicilio", "Teléfono", "Facebook", "Localidad"};
            //Creamos un arreglo y le pasamos rsmd, con getColumnCount() optenemos las cantidades de columnas de la BD.
            Object[] fila = new Object[rsmd.getColumnCount()];
            datos_usuario modelo = new datos_usuario(rs.getInt("id"),rs.getString("nombre"),rs.getString("apellidoP"),rs.getString("apellidoM"),rs.getString("cargo"),rs.getString("area"),rs.getString("tipoSolicitante"),rs.getString("motivo"),rs.getString("fecha"),rs.getString("contacto"),rs.getString("fechaAgenda"));
            while (rs.next()) {
                fila[0] = rs.getInt("id"); //Lo que hay entre comillas son los campos de la base de datos.
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("apellidoP");
                fila[3] = rs.getString("apellidoM");
                fila[4] = rs.getString("cargo");
                fila[5] = rs.getString("area");
                fila[6] = rs.getString("tipoSolicitante");
                fila[7] = rs.getString("Motivo");
                fila[8] = rs.getString("fecha");
                fila[9] = rs.getString("contacto");
                fila[10] = rs.getString("fechaAgenda");
                //modelo.addRow(fila); // Añade una fila al final del modelo de la tabla
            }


        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Error al intentar obtener los cliente:\n"
                   // + e, "Error en la operación", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    */



    //ACCION PARA BUSCAR

    @FXML
    public void buscarSolicitantes() {
        Connection c = ConexionBD.getConexion();
        try {
            Statement stm = c.createStatement();
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
        } catch (Exception e) {
            e.printStackTrace();
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


            String ruta = System.getProperty("user.home");
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Videos/Auditorio.pdf"));
            //Insertar image

            Image logo = Image.getInstance("C:\\Users\\nalai\\IdeaProjects\\escritorioAuditorio\\src\\main\\resources\\com\\example\\escritorioauditorio\\image/logoTecMM.png");
            logo.scaleToFit(250,250);
            logo.setAlignment(Chunk.ALIGN_CENTER);

            Image bordo = Image.getInstance("C:\\Users\\nalai\\IdeaProjects\\escritorioAuditorio\\src\\main\\resources\\com\\example\\escritorioauditorio\\image/triangulosMorados.png");
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
                Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/auditorio","root","");
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
                Scene escena = new Scene(loader.load());
                stage.setTitle("Finalizado");
                stage.setScene(escena);//agregar la esena a la ventana
                stage.showAndWait();
            } catch (Exception d){

            }

        }catch (DocumentException | FileNotFoundException e){

            try {
                Stage stage = new Stage();//Crear una nueva ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("error_PDF.fxml"));
                Scene escena = new Scene(loader.load());
                stage.setTitle("ERROR");
                stage.setScene(escena);//agregar la esena a la ventana
                stage.showAndWait();
            } catch (Exception d){

            }


            System.out.println("Error en generar PDFfffff" + e);

        }catch (IOException e){
            System.out.println("Error en la imagen" + e);
        }

        tabGeneral.getSelectionModel().select(2);



    }
}