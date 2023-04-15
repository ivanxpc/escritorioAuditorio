package com.example.escritorioauditorio;

import Dominio.reporteEmpleados_PDF;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class testPDF {
    public static void main(String[] args) throws DocumentException, FileNotFoundException {
        Scanner leer = new Scanner(System.in);
        System.out.println("Bienvenido a la empresa IAPC!!");
        System.out.println("Desea ver el formato PDF de solicitantes? (Y/N) ");
        char respuesta = leer.nextLine().charAt(0);
        if (respuesta == 'y' || respuesta == 'Y'){
            reporteEmpleados_PDF reporte = new reporteEmpleados_PDF();
            reporte.crearDocumento();
            reporte.abrirDocumento();
            reporte.agregarTitulo("Reporte de Solicitantes");
            reporte.agreafrSaldoDeLinea();
            reporte.agregarParrafo("Registro de solicitudes de Prestamos de Auditorio:");
            reporte.agreafrSaldoDeLinea();
            reporte.agregarTablaEmpleados();
            reporte.crearDocumento();
            System.out.println("PDF creado exitosamente");
        }else {
            System.out.println("No desa ver el formato de solicitantes ");
        }
    }
}
