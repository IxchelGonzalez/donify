package DAO;

import Conexion.ConexionBD;
import Modelo.Asociacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AsociacionDAO {

    public int crearAsociacion(String nombre) {
        String sql = "INSERT INTO Asociaciones(nombre, ubicacion, verificacion) VALUES (?, '', 0)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al crear asociacion: " + e.getMessage());
        }

        return 0;
    }

    public List<Asociacion> obtenerAsociaciones() {
        List<Asociacion> asociaciones = new ArrayList<>();
        String sql = "SELECT id_asociacion, nombre, ubicacion, verificacion FROM Asociaciones";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                asociaciones.add(new Asociacion(
                        rs.getInt("id_asociacion"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion"),
                        rs.getBoolean("verificacion")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener asociaciones: " + e.getMessage());
        }

        return asociaciones;
    }

    public boolean actualizarNombreAsociacion(int idAsociacion, String nombre) {
        String sql = "UPDATE Asociaciones SET nombre = ? WHERE id_asociacion = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setInt(2, idAsociacion);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar nombre de asociacion: " + e.getMessage());
            return false;
        }
    }

    public boolean existeNombreAsociacion(String nombre) {
        String sql = "SELECT id_asociacion FROM Asociaciones WHERE nombre = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Error al verificar asociacion: " + e.getMessage());
            return false;
        }
    }
}
