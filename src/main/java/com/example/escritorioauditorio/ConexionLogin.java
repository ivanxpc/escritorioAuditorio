package com.example.escritorioauditorio;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

public class ConexionLogin{
    public Connection conexion;
    public Statement sentencia;
    public ResultSet resultado;

    public void ConectarBasedeDatos() {
        try {
            final String url_bd = "jdbc:mysql://localhost:3306/auditorio";
            conexion = DriverManager.getConnection(url_bd, "root", "12345");
            sentencia = conexion.createStatement();
        } catch (SQLException ex) {
            System.out.println("NO SE PUDO HACER LA CONEXION");
        }
    }

    public void DesconectarBasedeDatos() {
        try {
            if (conexion != null) {
                if (sentencia != null) {
                    sentencia.close();
                }
                conexion.close();
            }
        } catch (SQLException ex) {
            System.out.println("ERROR AL INTENTAR DESCONECTAR");
            System.exit(1);
        }
    }

    public Connection getConnection() {
        return conexion;
    }
}
