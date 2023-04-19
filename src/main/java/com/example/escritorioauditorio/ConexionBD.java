package com.example.escritorioauditorio;

import java.sql.*;

public class ConexionBD {
    private static Connection c;

    public static Connection getConexion(){
        try {
            if (c==null) {
                String url = "jdbc:mysql://localhost:3306/auditorio";
                //Paso 1 = Crear una conexion;
                c = DriverManager.getConnection(url, "root", "12345");
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return c;
    }
}
