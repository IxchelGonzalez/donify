package DAO;

import Conexion.ConexionBD;
import Modelo.Asociacion;
import Modelo.Donador;
import Modelo.DonacionVista;
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
        String sql = "SELECT id_usuario, usuario FROM Usuarios WHERE tipo_usuario = 'donador'";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                donadores.add(new Donador(
                        rs.getInt("id_usuario"),
                        rs.getString("usuario"),
                        "",
                        ""
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

    /**
     * Tipos de prenda ya registrados en el catalogo (para sugerirlos al registrar
     * una donacion), sin repetir y ordenados alfabeticamente.
     */
    public List<String> obtenerTiposPrenda() {
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT DISTINCT tipo_prenda FROM Prendas ORDER BY tipo_prenda";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tipos.add(rs.getString("tipo_prenda"));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener tipos de prenda: " + e.getMessage());
        }

        return tipos;
    }

    public Prenda obtenerPrendaPorId(int idPrenda) {
        String sql = "SELECT id_prenda, tipo_prenda, estado_prenda, stock FROM Prendas WHERE id_prenda = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPrenda);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Prenda(
                            rs.getInt("id_prenda"),
                            rs.getString("tipo_prenda"),
                            rs.getString("estado_prenda"),
                            rs.getInt("stock")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la prenda: " + e.getMessage());
        }

        return null;
    }

    /**
     * Busca en el catalogo una prenda con ese tipo y estado (sin importar
     * mayusculas/espacios). Si no existe, la crea con stock inicial 0 y
     * devuelve su id. Devuelve 0 si ocurre un error.
     */
    public int obtenerOCrearIdPrenda(String tipoPrenda, String estadoPrenda) {
        String tipo = tipoPrenda == null ? "" : tipoPrenda.trim();
        String estado = estadoPrenda == null ? "" : estadoPrenda.trim();

        String sqlBuscar = "SELECT id_prenda FROM Prendas WHERE LOWER(tipo_prenda) = LOWER(?) "
                + "AND LOWER(estado_prenda) = LOWER(?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sqlBuscar)) {

            ps.setString(1, tipo);
            ps.setString(2, estado);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_prenda");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar la prenda: " + e.getMessage());
            return 0;
        }

        String sqlCrear = "INSERT INTO Prendas(tipo_prenda, estado_prenda, stock) VALUES (?, ?, 0)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sqlCrear, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, tipo);
            ps.setString(2, estado);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al crear la prenda: " + e.getMessage());
        }

        return 0;
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

    public List<DonacionVista> obtenerDonacionesCompletas() {
        List<DonacionVista> donaciones = new ArrayList<>();

        String sql = "SELECT di.id_donacion_ingreso, dd.id_donacion_detalle, "
                + "dn.id_usuario AS id_donador, dn.usuario AS nombre_donador, "
                + "a.id_asociacion, a.nombre AS nombre_asociacion, "
                + "p.id_prenda, p.tipo_prenda, p.estado_prenda, "
                + "dd.cantidad, di.fecha_donacion "
                + "FROM Donacion_Ingresos di "
                + "INNER JOIN Usuarios dn ON di.id_donador = dn.id_usuario "
                + "INNER JOIN Asociaciones a ON di.id_asociacion = a.id_asociacion "
                + "INNER JOIN Donacion_Detalle dd ON dd.id_donacion_ingreso = di.id_donacion_ingreso "
                + "INNER JOIN Prendas p ON dd.id_prenda = p.id_prenda "
                + "ORDER BY di.id_donacion_ingreso DESC";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                donaciones.add(new DonacionVista(
                        rs.getInt("id_donacion_ingreso"),
                        rs.getInt("id_donacion_detalle"),
                        rs.getInt("id_donador"),
                        rs.getString("nombre_donador"),
                        rs.getInt("id_asociacion"),
                        rs.getString("nombre_asociacion"),
                        rs.getInt("id_prenda"),
                        rs.getString("tipo_prenda") + " - " + rs.getString("estado_prenda"),
                        rs.getInt("cantidad"),
                        rs.getDate("fecha_donacion")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener donaciones: " + e.getMessage());
        }

        return donaciones;
    }

    public boolean actualizarDonacionCompleta(int idDonacionIngreso, int idDonacionDetalle,
            int idDonador, int idAsociacion, Date fechaDonacion,
            int idPrendaAnterior, int cantidadAnterior,
            int idPrendaNueva, int cantidadNueva) {

        Connection con = null;

        String sqlUpdateIngreso = "UPDATE Donacion_Ingresos SET id_donador = ?, id_asociacion = ?, "
                + "fecha_donacion = ? WHERE id_donacion_ingreso = ?";

        String sqlUpdateDetalle = "UPDATE Donacion_Detalle SET id_prenda = ?, cantidad = ? "
                + "WHERE id_donacion_detalle = ?";

        String sqlRestarStock = "UPDATE Prendas SET stock = stock - ? WHERE id_prenda = ?";
        String sqlSumarStock = "UPDATE Prendas SET stock = stock + ? WHERE id_prenda = ?";

        try {
            con = ConexionBD.conectar();

            if (con == null) {
                return false;
            }

            con.setAutoCommit(false);

            try (PreparedStatement psIngreso = con.prepareStatement(sqlUpdateIngreso)) {
                psIngreso.setInt(1, idDonador);
                psIngreso.setInt(2, idAsociacion);
                psIngreso.setDate(3, fechaDonacion);
                psIngreso.setInt(4, idDonacionIngreso);
                psIngreso.executeUpdate();
            }

            try (PreparedStatement psDetalle = con.prepareStatement(sqlUpdateDetalle)) {
                psDetalle.setInt(1, idPrendaNueva);
                psDetalle.setInt(2, cantidadNueva);
                psDetalle.setInt(3, idDonacionDetalle);
                psDetalle.executeUpdate();
            }

            try (PreparedStatement psRestar = con.prepareStatement(sqlRestarStock)) {
                psRestar.setInt(1, cantidadAnterior);
                psRestar.setInt(2, idPrendaAnterior);
                psRestar.executeUpdate();
            }

            try (PreparedStatement psSumar = con.prepareStatement(sqlSumarStock)) {
                psSumar.setInt(1, cantidadNueva);
                psSumar.setInt(2, idPrendaNueva);
                psSumar.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar donacion: " + e.getMessage());

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Error al revertir la actualizacion: " + ex.getMessage());
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

    public boolean eliminarDonacionCompleta(int idDonacionIngreso, int idDonacionDetalle,
            int idPrenda, int cantidad) {

        Connection con = null;

        String sqlRestarStock = "UPDATE Prendas SET stock = stock - ? WHERE id_prenda = ?";
        String sqlEliminarDetalle = "DELETE FROM Donacion_Detalle WHERE id_donacion_detalle = ?";
        String sqlEliminarIngreso = "DELETE FROM Donacion_Ingresos WHERE id_donacion_ingreso = ?";

        try {
            con = ConexionBD.conectar();

            if (con == null) {
                return false;
            }

            con.setAutoCommit(false);

            try (PreparedStatement psRestar = con.prepareStatement(sqlRestarStock)) {
                psRestar.setInt(1, cantidad);
                psRestar.setInt(2, idPrenda);
                psRestar.executeUpdate();
            }

            try (PreparedStatement psDetalle = con.prepareStatement(sqlEliminarDetalle)) {
                psDetalle.setInt(1, idDonacionDetalle);
                psDetalle.executeUpdate();
            }

            try (PreparedStatement psIngreso = con.prepareStatement(sqlEliminarIngreso)) {
                psIngreso.setInt(1, idDonacionIngreso);
                psIngreso.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar donacion: " + e.getMessage());

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Error al revertir la eliminacion: " + ex.getMessage());
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