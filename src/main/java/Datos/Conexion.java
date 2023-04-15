package Datos;

import java.sql.*;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/test";
    private static final String usuario = "root";
    private static final String contraseña = "";

    public static Connection abrirConexion() throws SQLException {
        return DriverManager.getConnection(URL,usuario,contraseña);
    }

    public static void cerrar(Connection conexion) throws SQLException {
        conexion.close();
    }

    public static void cerrar(Statement sentencia) throws SQLException{
        sentencia.close();
    }

    public static void cerrar(PreparedStatement sentencia) throws SQLException{
        sentencia.close();
    }

    public static void cerrar(ResultSet resultado) throws SQLException{
        resultado.close();
    }
}
