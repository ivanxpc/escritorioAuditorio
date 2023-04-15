package com.example.escritorioauditorio;

import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQL_Usuarios {

    public Boolean ValidarUsuario(usuarios usr)throws Exception {
        Connection c = ConexionBD.getConexion();
        ResultSet rs;
        PreparedStatement pst;
        String SQL = "SELECT id, nombre, correo, password FROM usuarios WHERE nombre= ?";

        pst = c.prepareStatement(SQL);
        pst.setString(1, usr.getNombre());
        rs = pst.executeQuery();

        if (rs.next()) {
            if (usr.getPassword().equals(rs.getString(2))) {
                usr.setId(rs.getInt(1));
                usr.setNombre(rs.getString(2));
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
