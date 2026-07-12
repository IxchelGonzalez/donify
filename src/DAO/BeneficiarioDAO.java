package DAO;

import Conexion.ConexionBD;
import Modelo.Beneficiario;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BeneficiarioDAO {

    public boolean crearBeneficiario(Beneficiario beneficiario) {
        String sql = "INSERT INTO Beneficiarios(nombre, sexo, fecha_ultima_recepcion) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, beneficiario.getNombre());
            ps.setString(2, beneficiario.getSexo());
            ps.setDate(3, beneficiario.getFechaUltimaRecepcion());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear beneficiario: " + e.getMessage());
            return false;
        }
    }

    public List<Beneficiario> obtenerBeneficiarios() {
        List<Beneficiario> beneficiarios = new ArrayList<>();
        String sql = "SELECT id_beneficiario, nombre, sexo, fecha_ultima_recepcion FROM Beneficiarios";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                beneficiarios.add(new Beneficiario(
                        rs.getInt("id_beneficiario"),
                        rs.getString("nombre"),
                        rs.getString("sexo"),
                        rs.getDate("fecha_ultima_recepcion")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener beneficiarios: " + e.getMessage());
        }

        return beneficiarios;
    }

    public boolean actualizarBeneficiario(Beneficiario beneficiario) {
        String sql = "UPDATE Beneficiarios SET nombre = ?, sexo = ?, fecha_ultima_recepcion = ? WHERE id_beneficiario = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, beneficiario.getNombre());
            ps.setString(2, beneficiario.getSexo());
            ps.setDate(3, beneficiario.getFechaUltimaRecepcion());
            ps.setInt(4, beneficiario.getIdBeneficiario());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar beneficiario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarBeneficiario(int idBeneficiario) {
        String sql = "DELETE FROM Beneficiarios WHERE id_beneficiario = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idBeneficiario);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar beneficiario: " + e.getMessage());
            return false;
        }
    }

    public boolean existeBeneficiario(String nombre) {
        String sql = "SELECT id_beneficiario FROM Beneficiarios WHERE nombre = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Error al verificar beneficiario: " + e.getMessage());
            return false;
        }
    }
}
