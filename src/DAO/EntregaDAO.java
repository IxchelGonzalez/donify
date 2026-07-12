package DAO;

import Conexion.ConexionBD;
import Modelo.Asociacion;
import Modelo.Beneficiario;
import Modelo.Prenda;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

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

    public List<Prenda> obtenerPrendasConStock() {
        List<Prenda> prendas = new ArrayList<>();
        String sql = "SELECT id_prenda, tipo_prenda, estado_prenda, stock FROM Prendas WHERE stock > 0";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                prendas.add(new Prenda(
                        rs.getInt("id_prenda"),
                        rs.getString("tipo_prenda"),
                        rs.getString("estado_prenda"),
                        rs.getInt("stock")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener prendas con stock: " + e.getMessage());
        }

        return prendas;
    }

    public boolean registrarEntregaCompleta(int idBeneficiario, int idAsociacion, Date fechaEntrega,
                                            int idPrenda, int cantidad) {
        Connection con = null;

        String sqlValidarStock = "SELECT stock FROM Prendas WHERE id_prenda = ?";
        String sqlEntrega = "INSERT INTO Entrega_Salida(id_beneficiario, id_asociacion, fecha_entrega) "
                          + "VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO Entrega_Detalle(id_entrega_salida, id_prenda, cantidad) "
                          + "VALUES (?, ?, ?)";
        String sqlDescontarStock = "UPDATE Prendas SET stock = stock - ? WHERE id_prenda = ?";
        String sqlActualizarBeneficiario = "UPDATE Beneficiarios SET fecha_ultima_recepcion = ? "
                                         + "WHERE id_beneficiario = ?";

        try {
            con = ConexionBD.conectar();

            if (con == null) {
                return false;
            }

            con.setAutoCommit(false);

            int stockActual = 0;

            try (PreparedStatement psStock = con.prepareStatement(sqlValidarStock)) {
                psStock.setInt(1, idPrenda);

                try (ResultSet rs = psStock.executeQuery()) {
                    if (rs.next()) {
                        stockActual = rs.getInt("stock");
                    }
                }
            }

            if (stockActual < cantidad) {
                con.rollback();
                return false;
            }

            int idEntregaSalida = 0;

            try (PreparedStatement psEntrega = con.prepareStatement(sqlEntrega, Statement.RETURN_GENERATED_KEYS)) {
                psEntrega.setInt(1, idBeneficiario);
                psEntrega.setInt(2, idAsociacion);
                psEntrega.setDate(3, fechaEntrega);
                psEntrega.executeUpdate();

                try (ResultSet rs = psEntrega.getGeneratedKeys()) {
                    if (rs.next()) {
                        idEntregaSalida = rs.getInt(1);
                    }
                }
            }

            if (idEntregaSalida == 0) {
                con.rollback();
                return false;
            }

            try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
                psDetalle.setInt(1, idEntregaSalida);
                psDetalle.setInt(2, idPrenda);
                psDetalle.setInt(3, cantidad);
                psDetalle.executeUpdate();
            }

            try (PreparedStatement psDescontar = con.prepareStatement(sqlDescontarStock)) {
                psDescontar.setInt(1, cantidad);
                psDescontar.setInt(2, idPrenda);
                psDescontar.executeUpdate();
            }

            try (PreparedStatement psBeneficiario = con.prepareStatement(sqlActualizarBeneficiario)) {
                psBeneficiario.setDate(1, fechaEntrega);
                psBeneficiario.setInt(2, idBeneficiario);
                psBeneficiario.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar entrega: " + e.getMessage());

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Error al revertir entrega: " + ex.getMessage());
            }

            return false;

        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
    }
}
