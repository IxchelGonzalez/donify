package DAO;

import Conexion.ConexionBD;
import Modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public boolean crearUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuarios(usuario, contrasena, curp, tipo_usuario, id_asociacion) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getContrasena());
            ps.setString(3, usuario.getCurp());
            ps.setString(4, usuario.getTipoUsuario());

            if (usuario.tieneAsociacion()) {
                ps.setInt(5, usuario.getIdAsociacion());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear usuario: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id_usuario, usuario, contrasena, curp, tipo_usuario, id_asociacion FROM Usuarios";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("usuario"),
                        rs.getString("contrasena"),
                        rs.getString("curp"),
                        rs.getString("tipo_usuario"),
                        rs.getInt("id_asociacion")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE Usuarios SET usuario = ?, contrasena = ?, curp = ?, tipo_usuario = ?, "
                + "id_asociacion = ? WHERE id_usuario = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getContrasena());
            ps.setString(3, usuario.getCurp());
            ps.setString(4, usuario.getTipoUsuario());

            if (usuario.tieneAsociacion()) {
                ps.setInt(5, usuario.getIdAsociacion());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            ps.setInt(6, usuario.getIdUsuario());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM Usuarios WHERE id_usuario = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean existeUsuario(String usuario) {
        String sql = "SELECT id_usuario FROM Usuarios WHERE usuario = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.out.println("Error al verificar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean existeCurp(String curp) {
        String sql = "SELECT id_usuario FROM Usuarios WHERE curp = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, curp);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.out.println("Error al verificar CURP: " + e.getMessage());
            return false;
        }
    }
}
