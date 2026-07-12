package DAO;

import Conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReporteGeneralDAO {

    public int obtenerTotalDonaciones() {
        String sql = "SELECT COUNT(*) AS total FROM Donacion_Ingresos";
        return obtenerEntero(sql);
    }

    public int obtenerTotalPrendasRecibidas() {
        String sql = "SELECT IFNULL(SUM(cantidad), 0) AS total FROM Donacion_Detalle";
        return obtenerEntero(sql);
    }

    public int obtenerTotalEntregas() {
        String sql = "SELECT COUNT(*) AS total FROM Entrega_Salida";
        return obtenerEntero(sql);
    }

    public int obtenerTotalPrendasEntregadas() {
        String sql = "SELECT IFNULL(SUM(cantidad), 0) AS total FROM Entrega_Detalle";
        return obtenerEntero(sql);
    }

    public int obtenerTotalBeneficiarios() {
        String sql = "SELECT COUNT(*) AS total FROM Beneficiarios";
        return obtenerEntero(sql);
    }

    public int obtenerStockTotalDisponible() {
        String sql = "SELECT IFNULL(SUM(stock), 0) AS total FROM Prendas";
        return obtenerEntero(sql);
    }

    public String obtenerPrendaMayorStock() {
        String sql = "SELECT CONCAT(tipo_prenda, ' - ', estado_prenda, ' (', stock, ')') AS resultado "
                   + "FROM Prendas ORDER BY stock DESC LIMIT 1";
        return obtenerTexto(sql);
    }

    public String obtenerBeneficiarioMasEntregas() {
        String sql = "SELECT CONCAT(b.nombre, ' (', COUNT(es.id_entrega_salida), ')') AS resultado "
                   + "FROM Beneficiarios b "
                   + "INNER JOIN Entrega_Salida es ON b.id_beneficiario = es.id_beneficiario "
                   + "GROUP BY b.id_beneficiario, b.nombre "
                   + "ORDER BY COUNT(es.id_entrega_salida) DESC LIMIT 1";
        return obtenerTexto(sql);
    }

    private int obtenerEntero(String sql) {
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener reporte numerico: " + e.getMessage());
        }

        return 0;
    }

    private String obtenerTexto(String sql) {
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("resultado");
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener reporte de texto: " + e.getMessage());
        }

        return "Sin datos";
    }
}
