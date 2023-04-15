package com.example.escritorioauditorio;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {

    private static Connection c;

    public static Connection getConexion(){
        try {
            if (c==null) {
                String url = "jdbc:mysql://localhost:3306/auditorio";
                c = DriverManager.getConnection(url, "root", "");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }
}
