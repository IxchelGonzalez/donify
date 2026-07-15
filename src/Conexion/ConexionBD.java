//Realiza la conexion a la base de datos generada en workbench
package Conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConexionBD {
    public static Connection conectar(){
        Connection con = null;
        try {
            String url = "jdbc:mysql://localhost:3306/proyectofinaldonify";
            String usuario = "root";
            String password = "";
            con = DriverManager.getConnection(url, usuario, password);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    return con;
}
}