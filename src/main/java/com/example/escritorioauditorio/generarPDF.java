package com.example.escritorioauditorio;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class generarPDF {
    Document documento = new Document();

    public void metodoGenerar_PDF() {
       // documento = new Document(PageSize.A4, 35, 30 ,50 ,50);
        Document documento = new Document();
        //documento.setPageSize(PageSize.A4.rotate());
        //documento.setMargins(15, 15, 15, 15);

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

            parrafo.setFont(FontFactory.getFont("Tahoma", 18, Font.BOLD, BaseColor.BLACK));
            parrafo.add("Registro de agenda del auditorio \n\n");
            parrafo.setAlignment(Paragraph.ALIGN_CENTER);
            parrafo.add("Datos del Solicitante: \n\n");

            documento.open();
            documento.add(logo);
            documento.add(bordo);
            documento.add(parrafo);


            //PdfPTable tabla = new PdfPTable(5);

            //tabla.SetWidths(values);

            PdfPTable tabla = new PdfPTable(7);
            float[] values = new float[7];
            values[0] = 70;
            values[1] = 200;
            values[2] = 200;
            values[3] = 200;

            values[4] = 200;
            values[5] = 200;
            values[6] = 150;

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
                PreparedStatement pst = cn.prepareStatement("SELECT * FROM datosusuario WHERE id= 3" );

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
                        //tabla.addCell(rs.getString(10));
                        //tabla.addCell(rs.getString(8));
                        //tabla.addCell(rs.getString(9));
                        espacio.add("\n" );
                        contacto.add("                       Contacto:   "+rs.getString(10));
                        motivo.add("                       Motivo:   "+rs.getString(8));
                        fecha.add("                       Fecha de elaboracion de solicitud:   "+rs.getString(9));
                        //parrafo.add(rs.getString(8));
                        //tabla.addCell(rs.getString(11));
                        //tabla.addCell(rs.getString(12));
                    }while (rs.next());
                    documento.add(tabla);
                    documento.add(espacio);
                    documento.add(contacto);
                    documento.add(motivo);
                    documento.add(fecha);

                }

            }catch (DocumentException | SQLException e){
                System.out.println("Error en generar PDF" + e);
            }

            documento.close();
            System.out.println("Documento creado");

        }catch (DocumentException | FileNotFoundException e){
            System.out.println("Error en generar PDF" + e);

        }catch (IOException e){
            System.out.println("Error en la imagen" + e);
        }
    }

    public static void main(String[] args) {

        generarPDF generar = new generarPDF();

        generar.metodoGenerar_PDF();

    }
}
