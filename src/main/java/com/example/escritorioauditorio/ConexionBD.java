package com.example.escritorioauditorio;

import java.sql.*;

public class ConexionBD {
    public static Connection c;

    public static Connection getConexion(){
        try {
            if (c!=null) {
                c.close();
            }
            c=null;
            String url = "jdbc:mysql://65.99.252.253:3306/eduwitco_auditorio";
            //Paso 1 = Crear una conexion;
            c = DriverManager.getConnection(url, "eduwitco_auditorio", "Ivan.098&$%");
        }catch (Exception e){
            e.printStackTrace();

        }
        return c;
    }
    //Hacer un metodo de refrescar
}
