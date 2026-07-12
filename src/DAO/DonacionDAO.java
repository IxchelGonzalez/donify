package DAO;

import Conexion.ConexionBD;
import Modelo.Asociacion;
import Modelo.Donador;
import Modelo.Prenda;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DonacionDAO {

    public int registrarDonador(Donador donador) {
        String sql = "INSERT INTO Donadores(nombre, correo, telefono) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, donador.getNombre());
            ps.setString(2, donador.getCorreo());
            ps.setString(3, donador.getTelefono());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar donador: " + e.getMessage());
        }

        return 0;
    }

    public List<Donador> obtenerDonadores() {
        List<Donador> donadores = new ArrayList<>();
        String sql = "SELECT id_donador, nombre, correo, telefono FROM Donadores";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                donadores.add(new Donador(
                        rs.getInt("id_donador"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener donadores: " + e.getMessage());
        }

        return donadores;
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

    public List<Prenda> obtenerPrendas() {
        List<Prenda> prendas = new ArrayList<>();
        String sql = "SELECT id_prenda, tipo_prenda, estado_prenda, stock FROM Prendas";

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
            System.out.println("Error al obtener prendas: " + e.getMessage());
        }

        return prendas;
    }

    public boolean registrarDonacionCompleta(int idDonador, int idAsociacion, Date fechaDonacion,
                                             int idPrenda, int cantidad) {
        Connection con = null;

        String sqlIngreso = "INSERT INTO Donacion_Ingresos(id_donador, id_asociacion, fecha_donacion) "
                          + "VALUES (?, ?, ?)";

        String sqlDetalle = "INSERT INTO Donacion_Detalle(id_donacion_ingreso, id_prenda, cantidad) "
                          + "VALUES (?, ?, ?)";

        String sqlActualizarStock = "UPDATE Prendas SET stock = stock + ? WHERE id_prenda = ?";

        try {
            con = ConexionBD.conectar();

            if (con == null) {
                return false;
            }

            con.setAutoCommit(false);

            int idDonacionIngreso = 0;

            try (PreparedStatement psIngreso = con.prepareStatement(sqlIngreso, Statement.RETURN_GENERATED_KEYS)) {
                psIngreso.setInt(1, idDonador);
                psIngreso.setInt(2, idAsociacion);
                psIngreso.setDate(3, fechaDonacion);
                psIngreso.executeUpdate();

                try (ResultSet rs = psIngreso.getGeneratedKeys()) {
                    if (rs.next()) {
                        idDonacionIngreso = rs.getInt(1);
                    }
                }
            }

            if (idDonacionIngreso == 0) {
                con.rollback();
                return false;
            }

            try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
                psDetalle.setInt(1, idDonacionIngreso);
                psDetalle.setInt(2, idPrenda);
                psDetalle.setInt(3, cantidad);
                psDetalle.executeUpdate();
            }

            try (PreparedStatement psStock = con.prepareStatement(sqlActualizarStock)) {
                psStock.setInt(1, cantidad);
                psStock.setInt(2, idPrenda);
                psStock.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar donacion: " + e.getMessage());

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Error al revertir la donacion: " + ex.getMessage());
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