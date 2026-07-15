//clase que conecta a la base para el login 
package DAO;

import Conexion.ConexionBD;
import Modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {
 //metodo para obetenr a los usuarios por medio de sus credenciales 
    public Usuario obtenerUsuarioPorCredenciales(String usuario, String contrasena) {
        String sql = "SELECT id_usuario, usuario, contrasena, curp, tipo_usuario "
                   + "FROM Usuarios WHERE usuario = ? AND contrasena = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.trim());
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("usuario"),
                            rs.getString("contrasena"),
                            rs.getString("curp"),
                            rs.getString("tipo_usuario")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al iniciar sesion: " + e.getMessage());
        }

        return null;
    }
}
