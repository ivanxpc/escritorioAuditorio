package com.example.escritorioauditorio;

import java.sql.*;

public class ConexionBD {
    private static Connection c;

    public static Connection getConexion(){
        try {
            if (c==null) {
                //Original
                //Esto es la conexion
               // String url = "jdbc:mysql://localhost:3306/auditorio";
                String url = "jdbc:mysql://65.99.252.253:3306/eduwitco_auditorio";
                //Paso 1 = Crear una conexion;
                c = DriverManager.getConnection(url, "eduwitco_auditorio", "Ivan.098&$%");
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return c;
    }
}
