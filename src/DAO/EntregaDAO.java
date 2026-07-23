package DAO;
 
import Conexion.ConexionBD;
import Modelo.Asociacion;
import Modelo.Beneficiario;
import Modelo.EntregaSalida;
import Modelo.EntregaVista;
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
            System.out.println("Error al obtener prenda: " + e.getMessage());
        }
 
        return null;
    }
 
    public boolean registrarEntregaCompleta(int idBeneficiario, int idAsociacion, Date fechaEntrega,
                                            int idPrenda, int cantidad) {
        Connection con = null;
 
        String sqlValidarStock = "SELECT stock FROM Prendas WHERE id_prenda = ?";
        String sqlEntrega = "INSERT INTO Entrega_Salida(id_beneficiario, id_asociacion, fecha_entrega, estado) "
                          + "VALUES (?, ?, ?, ?)";
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
                psEntrega.setString(4, EntregaSalida.ESTADO_PENDIENTE);
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
 
    /**
     * Obtiene las entregas completas. Si idAsociacion es null se obtienen las de todas
     * las instituciones; si se especifica, solo las de esa institucion.
     */
    public List<EntregaVista> obtenerEntregasCompletas(Integer idAsociacion) {
        List<EntregaVista> entregas = new ArrayList<>();
 
        StringBuilder sql = new StringBuilder(
                "SELECT es.id_entrega_salida, ed.id_entrega_detalle, "
                + "b.id_beneficiario, b.nombre AS nombre_beneficiario, "
                + "a.id_asociacion, a.nombre AS nombre_asociacion, "
                + "p.id_prenda, p.tipo_prenda, p.estado_prenda, "
                + "ed.cantidad, es.fecha_entrega, es.estado "
                + "FROM Entrega_Salida es "
                + "INNER JOIN Beneficiarios b ON es.id_beneficiario = b.id_beneficiario "
                + "INNER JOIN Asociaciones a ON es.id_asociacion = a.id_asociacion "
                + "INNER JOIN Entrega_Detalle ed ON es.id_entrega_salida = ed.id_entrega_salida "
                + "INNER JOIN Prendas p ON ed.id_prenda = p.id_prenda ");
 
        if (idAsociacion != null) {
            sql.append("WHERE es.id_asociacion = ? ");
        }
 
        sql.append("ORDER BY es.id_entrega_salida DESC");
 
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
 
            if (idAsociacion != null) {
                ps.setInt(1, idAsociacion);
            }
 
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    entregas.add(new EntregaVista(
                            rs.getInt("id_entrega_salida"),
                            rs.getInt("id_entrega_detalle"),
                            rs.getInt("id_beneficiario"),
                            rs.getString("nombre_beneficiario"),
                            rs.getInt("id_asociacion"),
                            rs.getString("nombre_asociacion"),
                            rs.getInt("id_prenda"),
                            rs.getString("tipo_prenda") + " - " + rs.getString("estado_prenda"),
                            rs.getInt("cantidad"),
                            rs.getDate("fecha_entrega"),
                            rs.getString("estado")
                    ));
                }
            }
 
        } catch (SQLException e) {
            System.out.println("Error al obtener entregas: " + e.getMessage());
        }
 
        return entregas;
    }
 
    private String obtenerEstadoEntrega(Connection con, int idEntregaSalida) throws SQLException {
        String sql = "SELECT estado FROM Entrega_Salida WHERE id_entrega_salida = ?";
 
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEntregaSalida);
 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("estado");
                }
            }
        }
 
        return null;
    }
 
    /**
     * Actualiza una entrega existente. Solo se permite si la entrega aun se encuentra
     * en estado Pendiente. Ajusta el stock de prendas segun la diferencia entre la
     * prenda/cantidad anterior y la nueva.
     */
    public boolean actualizarEntregaCompleta(int idEntregaSalida, int idEntregaDetalle,
            int idBeneficiario, int idAsociacion, Date fechaEntrega, String nuevoEstado,
            int idPrendaAnterior, int cantidadAnterior,
            int idPrendaNueva, int cantidadNueva) {
 
        Connection con = null;
 
        String sqlUpdateEntrega = "UPDATE Entrega_Salida SET id_beneficiario = ?, id_asociacion = ?, "
                + "fecha_entrega = ?, estado = ? WHERE id_entrega_salida = ?";
 
        String sqlUpdateDetalle = "UPDATE Entrega_Detalle SET id_prenda = ?, cantidad = ? "
                + "WHERE id_entrega_detalle = ?";
 
        String sqlSumarStock = "UPDATE Prendas SET stock = stock + ? WHERE id_prenda = ?";
        String sqlRestarStock = "UPDATE Prendas SET stock = stock - ? WHERE id_prenda = ?";
        String sqlValidarStock = "SELECT stock FROM Prendas WHERE id_prenda = ?";
 
        try {
            con = ConexionBD.conectar();
 
            if (con == null) {
                return false;
            }
 
            con.setAutoCommit(false);
 
            String estadoActual = obtenerEstadoEntrega(con, idEntregaSalida);
 
            if (estadoActual == null || !EntregaSalida.ESTADO_PENDIENTE.equalsIgnoreCase(estadoActual.trim())) {
                con.rollback();
                return false;
            }
 
            // Devolver al stock la cantidad de la prenda anterior
            try (PreparedStatement psSumar = con.prepareStatement(sqlSumarStock)) {
                psSumar.setInt(1, cantidadAnterior);
                psSumar.setInt(2, idPrendaAnterior);
                psSumar.executeUpdate();
            }
 
            // Validar que exista stock suficiente de la nueva prenda tras la devolucion
            int stockDisponible = 0;
 
            try (PreparedStatement psStock = con.prepareStatement(sqlValidarStock)) {
                psStock.setInt(1, idPrendaNueva);
 
                try (ResultSet rs = psStock.executeQuery()) {
                    if (rs.next()) {
                        stockDisponible = rs.getInt("stock");
                    }
                }
            }
 
            if (stockDisponible < cantidadNueva) {
                con.rollback();
                return false;
            }
 
            try (PreparedStatement psRestar = con.prepareStatement(sqlRestarStock)) {
                psRestar.setInt(1, cantidadNueva);
                psRestar.setInt(2, idPrendaNueva);
                psRestar.executeUpdate();
            }
 
            try (PreparedStatement psEntrega = con.prepareStatement(sqlUpdateEntrega)) {
                psEntrega.setInt(1, idBeneficiario);
                psEntrega.setInt(2, idAsociacion);
                psEntrega.setDate(3, fechaEntrega);
                psEntrega.setString(4, nuevoEstado);
                psEntrega.setInt(5, idEntregaSalida);
                psEntrega.executeUpdate();
            }
 
            try (PreparedStatement psDetalle = con.prepareStatement(sqlUpdateDetalle)) {
                psDetalle.setInt(1, idPrendaNueva);
                psDetalle.setInt(2, cantidadNueva);
                psDetalle.setInt(3, idEntregaDetalle);
                psDetalle.executeUpdate();
            }
 
            con.commit();
            return true;
 
        } catch (SQLException e) {
            System.out.println("Error al actualizar entrega: " + e.getMessage());
 
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
 
    /**
     * Elimina una entrega (solo si aun esta Pendiente) y devuelve la cantidad entregada
     * al stock de la prenda correspondiente.
     */
    public boolean eliminarEntregaCompleta(int idEntregaSalida, int idEntregaDetalle,
            int idPrenda, int cantidad) {
 
        Connection con = null;
 
        String sqlSumarStock = "UPDATE Prendas SET stock = stock + ? WHERE id_prenda = ?";
        String sqlEliminarDetalle = "DELETE FROM Entrega_Detalle WHERE id_entrega_detalle = ?";
        String sqlEliminarEntrega = "DELETE FROM Entrega_Salida WHERE id_entrega_salida = ?";
 
        try {
            con = ConexionBD.conectar();
 
            if (con == null) {
                return false;
            }
 
            con.setAutoCommit(false);
 
            String estadoActual = obtenerEstadoEntrega(con, idEntregaSalida);
 
            if (estadoActual == null || !EntregaSalida.ESTADO_PENDIENTE.equalsIgnoreCase(estadoActual.trim())) {
                con.rollback();
                return false;
            }
 
            try (PreparedStatement psSumar = con.prepareStatement(sqlSumarStock)) {
                psSumar.setInt(1, cantidad);
                psSumar.setInt(2, idPrenda);
                psSumar.executeUpdate();
            }
 
            try (PreparedStatement psDetalle = con.prepareStatement(sqlEliminarDetalle)) {
                psDetalle.setInt(1, idEntregaDetalle);
                psDetalle.executeUpdate();
            }
 
            try (PreparedStatement psEntrega = con.prepareStatement(sqlEliminarEntrega)) {
                psEntrega.setInt(1, idEntregaSalida);
                psEntrega.executeUpdate();
            }
 
            con.commit();
            return true;
 
        } catch (SQLException e) {
            System.out.println("Error al eliminar entrega: " + e.getMessage());
 
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