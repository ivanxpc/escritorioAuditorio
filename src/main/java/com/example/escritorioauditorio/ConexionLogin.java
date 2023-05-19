package com.example.escritorioauditorio;

import java.sql.*;

public class ConexionLogin{
    public Connection conexion;
    public Statement sentencia;
    public ResultSet resultado;

    public void ConectarBasedeDatos() {
        try {

            //Original
            //final String url_bd = "jdbc:mysql://localhost:3306/auditorio";
            final String url_bd = "jdbc:mysql://65.99.252.253:3306/eduwitco_auditorio";
            conexion = DriverManager.getConnection(url_bd, "eduwitco_auditorio", "Ivan.098&$%");
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
