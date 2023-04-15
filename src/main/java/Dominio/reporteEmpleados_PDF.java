package Dominio;

//import org.w3c.dom.Document;
import Datos.EmpleadoDAO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.control.Cell;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Scanner;

public class reporteEmpleados_PDF {

    Document documento;
    FileOutputStream fileOutputStream;

    //fuente de titulo y parrrafo
    Font funeteTitulo = FontFactory.getFont(FontFactory.TIMES_ROMAN,16);
    Font fuenteParrafo = FontFactory.getFont(FontFactory.HELVETICA,12);

    public void crearDocumento() throws DocumentException, FileNotFoundException {

        documento = new Document(PageSize.A4, 35, 30 ,50 ,50);

        //arcHIVO PDF que vamos a generar

        String ruta = System.getProperty("user.home");

        fileOutputStream = new FileOutputStream(ruta + "/Videos/Auditorio.pdf");


        // obtener una instancia del PdfWriter
        PdfWriter.getInstance(documento,fileOutputStream);
    }

    public void abrirDocumento(){
        documento.open();
    }

    public void agregarTitulo(String texto) throws DocumentException {
        PdfPTable tabla = new PdfPTable(1);
        PdfPCell celda = new PdfPCell(new Phrase(texto,funeteTitulo));
        celda.setColspan(5);
        celda.setBorderColor(BaseColor.WHITE);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);
        documento.add(tabla);
    }

    public void agregarParrafo(String texto) throws DocumentException {
        Paragraph parrafo = new Paragraph();
        parrafo.add(new Phrase(texto, fuenteParrafo));
        documento.add(parrafo);
    }

    public void agreafrSaldoDeLinea() throws DocumentException {
        Paragraph saltosLinea = new Paragraph();
        saltosLinea.add(new Phrase(Chunk.NEWLINE));
        saltosLinea.add(new Phrase(Chunk.NEWLINE));
        documento.add(saltosLinea);

    }

    public void agregarTablaEmpleados() throws DocumentException {
        PdfPTable tabla = new PdfPTable(10);
        tabla.addCell("ID");
        tabla.addCell("Nombre");
        tabla.addCell("Apellido Paterno");
        tabla.addCell("Apellido Materno");
        tabla.addCell("Cargo");
        tabla.addCell("Area");
        tabla.addCell("Tipo de Solicitante");
        tabla.addCell("Motivo");
        tabla.addCell("Fecha de Tramite");
        tabla.addCell("Contacto de Solicitante");

        EmpleadoDAO empleadoDAO = new EmpleadoDAO();
        List<datosUsuario_Mysql> emplados = empleadoDAO.mostrarEmpleados();

        for (datosUsuario_Mysql empleado: emplados){
            tabla.addCell(String.valueOf(empleado.getId()));
            tabla.addCell(empleado.getNombre());
            tabla.addCell(empleado.getApellidoP());
            tabla.addCell(empleado.getApellidoM());
            tabla.addCell(empleado.getCargo());
            tabla.addCell(empleado.getArea());
            tabla.addCell(empleado.getTipoSolicitante());
            tabla.addCell(empleado.getMotivo());
            tabla.addCell(empleado.getFecha());
            tabla.addCell(empleado.getContacto());


        }

        documento.add(tabla);
        cerrarDocumento();

    }

    public void cerrarDocumento(){
        documento.close();
    }


}
