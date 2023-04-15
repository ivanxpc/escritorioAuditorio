package Datos;

import Dominio.datosUsuario_Mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    public static final String SQL_SELECT = "SELECT * FROM alumnos";

    public List<datosUsuario_Mysql> mostrarEmpleados(){
        Connection conexionBD = null;
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        datosUsuario_Mysql empleado = null;
        List<datosUsuario_Mysql> empleados = new ArrayList<>();
        try{
            conexionBD = Conexion.abrirConexion();
            //revisa AQUI
            sentencia = conexionBD.prepareStatement(SQL_SELECT);
            resultado = sentencia.executeQuery();
            while(resultado.next()){
                empleado = new datosUsuario_Mysql();
                empleado.setId(resultado.getInt(1));
                empleado.setNombre(resultado.getString(2));
               /* empleado.setApellidoP(resultado.getString(3));
                empleado.setApellidoM(resultado.getString(4));
                empleado.setCargo(resultado.getString(5));
                empleado.setArea(resultado.getString(6));
                empleado.setTipoSolicitante(resultado.getString(7));
                empleado.setMotivo(resultado.getString(8));
                empleado.setFecha(resultado.getString(9));
                empleado.setContacto(resultado.getString(10));
*/
                empleados.add(empleado);
            }
        }catch(SQLException e){
            e.printStackTrace(System.out);
        }finally{
            try{
                Conexion.cerrar(resultado);
                Conexion.cerrar(sentencia);
                Conexion.cerrar(conexionBD);
            }catch (SQLException e){
                e.printStackTrace(System.out);
            }
        }
        return empleados;
    }
}
