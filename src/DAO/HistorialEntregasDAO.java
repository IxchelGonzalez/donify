package DAO;

import Conexion.ConexionBD;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class HistorialEntregasDAO {

    /**
     * Historial de entregas. Si idAsociacion es null se incluyen las de todas las
     * instituciones; si se especifica, solo el historial de esa institucion.
     */
    public DefaultTableModel obtenerHistorialEntregas(Integer idAsociacion) {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{
                    "ID Entrega",
                    "Beneficiario",
                    "Institucion",
                    "Prenda",
                    "Estado prenda",
                    "Cantidad",
                    "Fecha entrega",
                    "Estado entrega"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        StringBuilder sql = new StringBuilder(
                "SELECT es.id_entrega_salida, "
                + "b.nombre AS beneficiario, "
                + "a.nombre AS asociacion, "
                + "p.tipo_prenda, "
                + "p.estado_prenda, "
                + "ed.cantidad, "
                + "es.fecha_entrega, "
                + "es.estado "
                + "FROM Entrega_Salida es "
                + "INNER JOIN Beneficiarios b ON es.id_beneficiario = b.id_beneficiario "
                + "INNER JOIN Asociaciones a ON es.id_asociacion = a.id_asociacion "
                + "INNER JOIN Entrega_Detalle ed ON es.id_entrega_salida = ed.id_entrega_salida "
                + "INNER JOIN Prendas p ON ed.id_prenda = p.id_prenda ");

        if (idAsociacion != null) {
            sql.append("WHERE es.id_asociacion = ? ");
        }

        sql.append("ORDER BY es.fecha_entrega DESC");

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            if (idAsociacion != null) {
                ps.setInt(1, idAsociacion);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date fecha = rs.getDate("fecha_entrega");

                    modelo.addRow(new Object[]{
                        rs.getInt("id_entrega_salida"),
                        rs.getString("beneficiario"),
                        rs.getString("asociacion"),
                        rs.getString("tipo_prenda"),
                        rs.getString("estado_prenda"),
                        rs.getInt("cantidad"),
                        fecha == null ? "" : fecha.toString(),
                        rs.getString("estado")
                    });
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener historial de entregas: " + e.getMessage());
        }

        return modelo;
    }
}
