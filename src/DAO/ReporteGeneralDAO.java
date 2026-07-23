package DAO;

import Conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReporteGeneralDAO {

    public int obtenerTotalDonaciones(Integer idAsociacion) {
        if (idAsociacion == null) {
            return obtenerEntero("SELECT COUNT(*) AS total FROM Donacion_Ingresos", null);
        }
        return obtenerEntero(
                "SELECT COUNT(*) AS total FROM Donacion_Ingresos WHERE id_asociacion = ?", idAsociacion);
    }

    public int obtenerTotalPrendasRecibidas(Integer idAsociacion) {
        if (idAsociacion == null) {
            return obtenerEntero("SELECT IFNULL(SUM(cantidad), 0) AS total FROM Donacion_Detalle", null);
        }
        return obtenerEntero(
                "SELECT IFNULL(SUM(dd.cantidad), 0) AS total FROM Donacion_Detalle dd "
                + "INNER JOIN Donacion_Ingresos di ON dd.id_donacion_ingreso = di.id_donacion_ingreso "
                + "WHERE di.id_asociacion = ?", idAsociacion);
    }

    public int obtenerTotalEntregas(Integer idAsociacion) {
        if (idAsociacion == null) {
            return obtenerEntero("SELECT COUNT(*) AS total FROM Entrega_Salida", null);
        }
        return obtenerEntero(
                "SELECT COUNT(*) AS total FROM Entrega_Salida WHERE id_asociacion = ?", idAsociacion);
    }

    public int obtenerTotalPrendasEntregadas(Integer idAsociacion) {
        if (idAsociacion == null) {
            return obtenerEntero("SELECT IFNULL(SUM(cantidad), 0) AS total FROM Entrega_Detalle", null);
        }
        return obtenerEntero(
                "SELECT IFNULL(SUM(ed.cantidad), 0) AS total FROM Entrega_Detalle ed "
                + "INNER JOIN Entrega_Salida es ON ed.id_entrega_salida = es.id_entrega_salida "
                + "WHERE es.id_asociacion = ?", idAsociacion);
    }

    public int obtenerTotalBeneficiarios() {
        return obtenerEntero("SELECT COUNT(*) AS total FROM Beneficiarios", null);
    }

    public int obtenerStockTotalDisponible(Integer idAsociacion) {
        if (idAsociacion == null) {
            return obtenerEntero("SELECT IFNULL(SUM(stock), 0) AS total FROM Prendas", null);
        }
        return obtenerEntero(
                "SELECT IFNULL(SUM(don.total_donado - IFNULL(ent.total_entregado, 0)), 0) AS total FROM ("
                + "  SELECT dd.id_prenda, SUM(dd.cantidad) AS total_donado FROM Donacion_Detalle dd "
                + "  INNER JOIN Donacion_Ingresos di ON dd.id_donacion_ingreso = di.id_donacion_ingreso "
                + "  WHERE di.id_asociacion = ? GROUP BY dd.id_prenda"
                + ") don LEFT JOIN ("
                + "  SELECT ed.id_prenda, SUM(ed.cantidad) AS total_entregado FROM Entrega_Detalle ed "
                + "  INNER JOIN Entrega_Salida es ON ed.id_entrega_salida = es.id_entrega_salida "
                + "  WHERE es.id_asociacion = ? GROUP BY ed.id_prenda"
                + ") ent ON ent.id_prenda = don.id_prenda", idAsociacion, idAsociacion);
    }

    public String obtenerPrendaMayorStock(Integer idAsociacion) {
        if (idAsociacion == null) {
            return obtenerTexto(
                    "SELECT CONCAT(tipo_prenda, ' - ', estado_prenda, ' (', stock, ')') AS resultado "
                    + "FROM Prendas ORDER BY stock DESC LIMIT 1", null);
        }
        return obtenerTexto(
                "SELECT CONCAT(p.tipo_prenda, ' - ', p.estado_prenda, ' (', "
                + "(don.total_donado - IFNULL(ent.total_entregado, 0)), ')') AS resultado FROM Prendas p "
                + "INNER JOIN ("
                + "  SELECT dd.id_prenda, SUM(dd.cantidad) AS total_donado FROM Donacion_Detalle dd "
                + "  INNER JOIN Donacion_Ingresos di ON dd.id_donacion_ingreso = di.id_donacion_ingreso "
                + "  WHERE di.id_asociacion = ? GROUP BY dd.id_prenda"
                + ") don ON don.id_prenda = p.id_prenda LEFT JOIN ("
                + "  SELECT ed.id_prenda, SUM(ed.cantidad) AS total_entregado FROM Entrega_Detalle ed "
                + "  INNER JOIN Entrega_Salida es ON ed.id_entrega_salida = es.id_entrega_salida "
                + "  WHERE es.id_asociacion = ? GROUP BY ed.id_prenda"
                + ") ent ON ent.id_prenda = p.id_prenda "
                + "ORDER BY (don.total_donado - IFNULL(ent.total_entregado, 0)) DESC LIMIT 1",
                idAsociacion, idAsociacion);
    }

    public String obtenerBeneficiarioMasEntregas(Integer idAsociacion) {
        if (idAsociacion == null) {
            return obtenerTexto(
                    "SELECT CONCAT(b.nombre, ' (', COUNT(es.id_entrega_salida), ')') AS resultado "
                    + "FROM Beneficiarios b "
                    + "INNER JOIN Entrega_Salida es ON b.id_beneficiario = es.id_beneficiario "
                    + "GROUP BY b.id_beneficiario, b.nombre "
                    + "ORDER BY COUNT(es.id_entrega_salida) DESC LIMIT 1", null);
        }
        return obtenerTexto(
                "SELECT CONCAT(b.nombre, ' (', COUNT(es.id_entrega_salida), ')') AS resultado "
                + "FROM Beneficiarios b "
                + "INNER JOIN Entrega_Salida es ON b.id_beneficiario = es.id_beneficiario "
                + "WHERE es.id_asociacion = ? "
                + "GROUP BY b.id_beneficiario, b.nombre "
                + "ORDER BY COUNT(es.id_entrega_salida) DESC LIMIT 1", idAsociacion);
    }

    private int obtenerEntero(String sql, Integer... parametros) {
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            aplicarParametros(ps, parametros);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener reporte numerico: " + e.getMessage());
        }

        return 0;
    }

    private String obtenerTexto(String sql, Integer... parametros) {
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            aplicarParametros(ps, parametros);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("resultado");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener reporte de texto: " + e.getMessage());
        }

        return "Sin datos";
    }

    private void aplicarParametros(PreparedStatement ps, Integer... parametros) throws SQLException {
        if (parametros == null) {
            return;
        }

        int indice = 1;

        for (Integer parametro : parametros) {
            if (parametro != null) {
                ps.setInt(indice++, parametro);
            }
        }
    }
}
